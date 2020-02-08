package ru.bifacial.otp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface CommandBase
{
    boolean execute(final CommandSender p0, final Command p1, final String p2, final String[] p3);

    String getName();
}
