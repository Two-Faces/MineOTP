package ru.bifacial.otp;

import org.bukkit.ChatColor;

import java.io.File;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import org.jboss.aerogear.security.otp.Totp;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.bifacial.otp.listener.ListenerManager;
import ru.bifacial.otp.command.CommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class MineOTP extends JavaPlugin
{
    protected CommandHandler chandler;
    protected ListenerManager lmanager;
    private String table;
    private boolean isBusy;
    private boolean reloaded;
    private String secretKey;
    private HashMap<String, Boolean> needlist;
    private HashMap<String, String> authlist;
    private List<String> pexlist;
    
    public MineOTP() {
        this.isBusy = true;
        this.reloaded = false;
        this.needlist = new HashMap<>();
        this.authlist = new HashMap<>();
        this.pexlist = new ArrayList<>();
    }
    
    public void onLoad() {
        this.getLogger().info("Loading Configuration mananger...");
        try {
            Thread.sleep(500L);
        }
        catch (InterruptedException var2) {
            Logger.getLogger(MineOTP.class.getName()).log(Level.SEVERE, null, var2);
        }
    }
    
    public void onEnable() {
        if (!(new File(this.getDataFolder(), "config.yml")).exists()) {
            this.createConfig();
        }

        if (!this.reloaded && Bukkit.getOnlinePlayers().size() > 0) {
            this.reloaded = true;
        }
        this.chandler = new CommandHandler(this);
        this.lmanager = new ListenerManager(this);
        this.getLogger().info("Loading SQL manager...");
        this.table = this.getConfig().getString("managers.mysql.table");
        this.pexlist = this.getConfig().getStringList("main.pexlist");
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                MineOTP.this.updateCache();
            }
        }, 1200L, 1200L);
        this.setBusy(false);
    }
    
    public void updateCache() {
        this.secretKey = null;
        this.needlist.clear();
    }

    public void createConfig() {
        this.getConfig().options().header("Plugin by Bifacial");
        this.getConfig().set("general.debug-level", 0);

        this.getConfig().set("data.manager", "mysql");

        // MYSQL
        this.getConfig().set("managers.mysql.host", "localhost");
        this.getConfig().set("managers.mysql.port", "3306");
        this.getConfig().set("managers.mysql.database", "");
        this.getConfig().set("managers.mysql.username", "root");
        this.getConfig().set("managers.mysql.password", "");
        this.getConfig().set("managers.mysql.table", "table");

        // MAIN
        List<String> list = new ArrayList<>();
        list.add("*");

        this.getConfig().set("main.ifop", true);
        this.getConfig().set("main.pexlist", list);

        this.saveConfig();
    }
    
    public boolean hasOTP(final Player p) {
        if (this.secretKey == null) {
            this.getSecretKeyUser(p);
        }
        return this.secretKey != null;
    }
    
    public boolean isLoggedUser(final Player p) {
        if (this.authlist.containsKey(p.getUniqueId().toString())) {
            try {
                final String ip = p.getAddress().toString().split("/")[1].split(":")[0];
                if (this.authlist.get(p.getUniqueId().toString()).equalsIgnoreCase(ip)) {
                    return false;
                }
                this.authlist.remove(p.getUniqueId().toString());
            }
            catch (Exception var3) {
                var3.printStackTrace();
                return true;
            }
        }
        return true;
    }
    
    private void sendToSuperAdmins(final String message) {
        for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.isOnline() && p.hasPermission("mineotp.admin.message")) {
                p.sendMessage(__(message));
            }
        }
    }
    
    public boolean authPlayer(final Player p, final String code) {
        if (this.hasOTP(p)) {
            final Totp totp = new Totp(this.secretKey);
            if (totp.verify(code)) {
                try {
                    final String ip = p.getAddress().toString().split("/")[1].split(":")[0];
                    this.authlist.put(p.getUniqueId().toString(), ip);
                    this.sendToSuperAdmins("\u0418\u0433\u0440\u043e\u043a " + p.getName() + " \u0430\u0432\u0442\u043e\u0440\u0438\u0437\u043e\u0432\u0430\u043b\u0441\u044f \u0441 IP: " + ip);
                    return true;
                }
                catch (Exception var5) {
                    var5.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }
    
    public boolean needToOTP(final Player p) {
        if (p == null) {
            return false;
        }
        if (this.getConfig().getBoolean("main.ifop") && p.isOp()) {
            return true;
        }
        boolean check = false;
        if (this.needlist.containsKey(p.getUniqueId().toString())) {
            return this.needlist.get(p.getUniqueId().toString());
        }
        
        for(Object pex : this.pexlist) {
            if (p.hasPermission((String)pex)) {
                check = true;
                break;
            }
        }
        this.needlist.put(p.getUniqueId().toString(), check);
        return check;
    }
    
    public void getSecretKeyUser(final Player p) {
        final String url = "jdbc:mysql://" + this.getConfig().getString("managers.mysql.host") + ":" + this.getConfig().getString("managers.mysql.port") + "/" + this.getConfig().getString("managers.mysql.database");
        final String username = this.getConfig().getString("managers.mysql.username");
        final String password = this.getConfig().getString("managers.mysql.password");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            final Connection connection = DriverManager.getConnection(url, username, password);
            final PreparedStatement statement = connection.prepareStatement(String.format("SELECT * FROM `%s` WHERE `uuid` = ?", this.table));
            statement.setString(1, p.getUniqueId().toString());
            final ResultSet rs = statement.executeQuery();
            this.secretKey = (rs.first() ? rs.getString("otp_key") : null);
            rs.close();
            statement.close();
            connection.close();
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static MineOTP getPlugin() {
        return (MineOTP)Bukkit.getPluginManager().getPlugin("MineOTP");
    }
    
    public static String __(final String encoded) {
        return ChatColor.translateAlternateColorCodes('&', "[&e" + getPlugin().getDescription().getName() + "&f] &6" + encoded);
    }
    
    public boolean isBusy() {
        return this.isBusy;
    }
    
    public void setBusy(final boolean busy) {
        this.isBusy = busy;
    }
}
