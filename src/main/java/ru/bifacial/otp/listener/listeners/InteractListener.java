package ru.bifacial.otp.listener.listeners;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockPlaceEvent;
import ru.bifacial.otp.MineOTP;
import org.bukkit.event.Listener;

public class InteractListener implements Listener
{
    private MineOTP plugin;
    
    public InteractListener(final MineOTP plugin_) {
        this.plugin = plugin_;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPlace(final BlockPlaceEvent e) {
        this.handleEvent(e.getPlayer(), e, "place");
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBreak(final BlockBreakEvent e) {
        this.handleEvent(e.getPlayer(), e, "break");
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(final PlayerInteractEvent e) {
        this.handleEvent(e.getPlayer(), e, "interact");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(final AsyncPlayerChatEvent e) {
        this.handleEvent(e.getPlayer(), e, "chat");
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerClick(final InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            this.handleEvent((Player)e.getWhoClicked(), e, "click");
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCMD(final PlayerCommandPreprocessEvent e) {
        if (!e.getMessage().toLowerCase().startsWith("/otpauth")) {
            this.handleEvent(e.getPlayer(), e, "cmd");
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(final PlayerMoveEvent e) {
        this.handleEvent(e.getPlayer(), e, "move");
    }

    private void handleEvent(final Player p, final Cancellable e, String event) {
        if (p != null && e != null && this.plugin.needToOTP(p) && this.plugin.isLoggedUser(p)) {
            p.sendMessage(MineOTP.__("\u0412\u044b \u043d\u0435 \u043c\u043e\u0436\u0435\u0442\u0435 \u0432\u044b\u043f\u043e\u043b\u043d\u0438\u0442\u044c \u044d\u0442\u043e! \u0418\u0441\u043f\u043e\u043b\u044c\u0437\u0443\u0439\u0442\u0435 /otpauth [key]"));

            if ("move".equals(event)) {
                p.teleport(LoginListener.coords);
            } else {
                e.setCancelled(true);
            }
        }
    }
}
