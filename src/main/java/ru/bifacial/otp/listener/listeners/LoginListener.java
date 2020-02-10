// 
// Decompiled by Procyon v0.5.36
// 

package ru.bifacial.otp.listener.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.bifacial.otp.MineOTP;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class LoginListener implements Listener
{
    private MineOTP plugin;
    
    public LoginListener(final MineOTP plugin_) {
        this.plugin = plugin_;
    }

    public static HashMap<UUID, Location> coords = new HashMap<>();
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();

        if (this.plugin.hasOTP(p)) {
            coords.put(p.getUniqueId(), p.getLocation());

            p.sendMessage(MineOTP.__("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u0443\u0439\u0442\u0435 /otpauth [key] - \u0447\u0442\u043e \u0431\u044b \u0432\u043e\u0439\u0442\u0438!"));
        }
    }
}
