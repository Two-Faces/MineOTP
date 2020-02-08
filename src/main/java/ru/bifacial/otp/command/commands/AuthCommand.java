package ru.bifacial.otp.command.commands;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.bifacial.otp.MineOTP;
import ru.bifacial.otp.command.CommandBase;
import ru.bifacial.otp.listener.listeners.LoginListener;

public class AuthCommand implements CommandBase
{
    private final MineOTP plugin;
    
    public AuthCommand(final MineOTP plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean execute(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (sender instanceof Player) {
            final Player p = (Player)sender;
            if (args.length == 1) {
                if (this.plugin.hasOTP(p)) {
                    if (this.plugin.isLoggedUser(p)) {
                        if (this.plugin.authPlayer(p, args[0])) {
                            LoginListener.coords = null;
                            p.sendMessage(MineOTP.__("\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0432\u043e\u0448\u043b\u0438!"));
                        }
                        else {
                            p.sendMessage(MineOTP.__("\u041d\u0435\u0432\u0435\u0440\u043d\u044b\u0439 \u043a\u043e\u0434!"));
                        }
                    }
                    else {
                        p.sendMessage(MineOTP.__("\u0412\u044b \u0443\u0436\u0435 \u0432\u043e\u0448\u043b\u0438!"));
                    }
                }
                else {
                    p.sendMessage(MineOTP.__("\u0412\u044b \u043d\u0435 \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u044b \u043a OTP! \u041e\u0431\u0440\u0430\u0442\u0438\u0442\u0435\u0441\u044c \u043a \u0430\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u0438!"));
                }
            }
            else {
                p.sendMessage(MineOTP.__("/otpauth [key]"));
            }
        }
        else {
            sender.sendMessage(MineOTP.__("\u041a\u043e\u043c\u0430\u043d\u0434\u0430 \u0434\u043e\u0441\u0442\u0443\u043f\u043d\u0430 \u0442\u043e\u043b\u044c\u043a\u043e \u0438\u0433\u0440\u043e\u043a\u0430\u043c!"));
        }
        return false;
    }
    
    @Override
    public String getName() {
        return "otpauth";
    }
}
