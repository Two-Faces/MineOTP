package ru.bifacial.otp.listener;

import org.bukkit.event.Listener;
import ru.bifacial.otp.MineOTP;
import ru.bifacial.otp.listener.listeners.InteractListener;
import ru.bifacial.otp.listener.listeners.LoginListener;
import java.util.HashMap;
import java.util.Map;

public class ListenerManager
{
    public ListenerManager(final MineOTP plugin) {
        Map<String, Listener> listeners = new HashMap<>();
        listeners.put("login", new LoginListener(plugin));
        listeners.put("interact", new InteractListener(plugin));
        for (final Listener l : listeners.values()) {
            plugin.getServer().getPluginManager().registerEvents(l, plugin);
        }
    }
}
