package ru.bifacial.otp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.bifacial.otp.MineOTP;
import ru.bifacial.otp.command.commands.AuthCommand;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandExecutor;

public class CommandHandler implements CommandExecutor
{
    private final MineOTP plugin;
    private final Map<String, CommandBase> commands;

    public CommandHandler(final MineOTP plugin) {
        this.commands = new HashMap<>();
        this.plugin = plugin;
        final CommandBase[] cmds = { new AuthCommand(this.plugin) };
        final CommandHandler chand = this;
        for (int j = cmds.length, i = 0; i < j; ++i) {
            final CommandBase cmd = cmds[i];
            this.commands.put(cmd.getName(), cmd);
            this.plugin.getCommand(cmd.getName()).setExecutor(chand);
        }
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (!this.isReload(commandLabel, args) && this.plugin.isBusy()) {
            sender.sendMessage(MineOTP.__("\u041f\u043b\u0430\u0433\u0438\u043d \u0437\u0430\u043d\u044f\u0442! \u041f\u043e\u043f\u0440\u043e\u0431\u0443\u0439\u0442\u0435 \u043f\u043e\u0437\u0436\u0435!"));
        }
        else {
            final CommandBase command = this.commands.get(commandLabel);
            if (command != null) {
                return command.execute(sender, cmd, commandLabel, args);
            }
        }
        return false;
    }

    private boolean isReload(final String cmd, final String[] args) {
        return cmd.equalsIgnoreCase("otpauth") && args.length == 1 && args[0].equalsIgnoreCase("reload");
    }
}
