package me.logan.campwarspv2.Listeners;

import me.logan.campwarspv2.Main;
import me.logan.campwarspv2.Utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GuiListener implements Listener {

    private Main plugin;
    ArrayList<UUID> tpQueue = new ArrayList<UUID>();

    public GuiListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShiftClick(InventoryClickEvent e) {
        if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.GREEN.toString() + ChatColor.BOLD + "Camps"))
            if (e.getClick() != ClickType.LEFT) {
                e.setCancelled(true);
                return;
            }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.GREEN.toString() + ChatColor.BOLD + "Camps")
                && e.getCurrentItem() != null) {
            e.setCancelled(true);
            UUID playerUUID = e.getWhoClicked().getUniqueId();
            ItemStack clickedItem = e.getCurrentItem();
            String blockName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

            if (plugin.getConfig().contains(blockName)) {
                Location targetLocation =getLocationFromConfig(blockName);
                if (targetLocation != null) {
                    if (tpQueue.contains(playerUUID)) {
                        e.getWhoClicked().sendMessage(ColorUtils.colorize("&cYou already have a tp request"));
                    } else {
                        tpQueue.add(playerUUID);
                        e.getWhoClicked().sendMessage(ColorUtils.colorize("&aYou will be teleported in 5 seconds"));
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            e.getWhoClicked().teleport(targetLocation);
                            e.getWhoClicked().sendMessage(ColorUtils.colorize("&aYou have been teleported"));
                        }, 100);
                        tpQueue.remove(playerUUID);

                    }
                }
            }
        }
    }

    private Location getLocationFromConfig(String campName) {
        String worldName = plugin.getConfig().getString(campName + ".World");
        double x = plugin.getConfig().getDouble(campName + ".X");
        double y = plugin.getConfig().getDouble(campName + ".Y");
        double z = plugin.getConfig().getDouble(campName + ".Z");
        float pitch = (float) plugin.getConfig().getDouble(campName + ".Pitch");
        float yaw = (float) plugin.getConfig().getDouble(campName + ".Yaw");

        return new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }
}



