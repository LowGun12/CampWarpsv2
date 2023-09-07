package me.logan.campwarspv2.Listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.logan.campwarspv2.Main;
import me.logan.campwarspv2.Utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class GuiListener implements Listener {

    private final Main plugin;
    private final Map<UUID, BukkitRunnable> teleportTasks = new HashMap<>();

    public GuiListener(Main plugin) {
        this.plugin = plugin;
    }

    // Method to check if a player is already teleporting
    private boolean isTeleporting(UUID playerUUID) {
        return teleportTasks.containsKey(playerUUID);
    }

    // Method to set teleporting status for a player
    private void setTeleporting(UUID playerUUID, boolean status) {
        if (!status) {
            teleportTasks.remove(playerUUID);
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        UUID playerUUID = e.getWhoClicked().getUniqueId();


        if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.GREEN.toString() + ChatColor.BOLD + plugin.getConfig().getString("GuiName"))
                && e.getCurrentItem() != null) {
            e.setCancelled(true);

            if (e.getWhoClicked() instanceof Player) {
                Player player = (Player) e.getWhoClicked();

                // Check if the player is already teleporting
                if (isTeleporting(playerUUID)) {
                    player.sendMessage(ColorUtils.colorize("&cYou are already teleporting. Please wait."));
                    return;
                }

                ItemStack clickedItem = e.getCurrentItem();
                ItemMeta itemMeta = clickedItem.getItemMeta();
                if (itemMeta != null && itemMeta.hasDisplayName()) {
                    String displayName = itemMeta.getDisplayName();
                    String configCampName = ChatColor.stripColor(displayName).toLowerCase();
                    if (plugin.getConfig().contains("camps." + configCampName)) {
                        String worldName = plugin.getConfig().getString("camps." + configCampName + ".World");
                        double x = plugin.getConfig().getDouble("camps." + configCampName + ".X");
                        double y = plugin.getConfig().getDouble("camps." + configCampName + ".Y");
                        double z = plugin.getConfig().getDouble("camps." + configCampName + ".Z");
                        float pitch = (float) plugin.getConfig().getDouble("camps." + configCampName + ".Pitch");
                        float yaw = (float) plugin.getConfig().getDouble("camps." + configCampName + ".Yaw");

                        // Set the player's teleporting status to true
                        setTeleporting(playerUUID, true);

                        player.sendMessage(ColorUtils.colorize("&aYou will be teleported in 5 seconds. Please do not move."));

                        BukkitRunnable teleportTask = new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (player.isOnline() && !player.isDead()) {
                                    player.teleport(new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch));
                                    player.sendMessage(ChatColor.GREEN + "Teleported to " + displayName);
                                }

                                // Set the player's teleporting status to false after teleporting or if they moved
                                setTeleporting(playerUUID, false);
                            }
                        };

                        teleportTasks.put(playerUUID, teleportTask);
                        teleportTask.runTaskLater(plugin, 100); // 100 ticks = 5 seconds
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (teleportTasks.containsKey(playerUUID)) {
            Location from = e.getFrom();
            Location to = e.getTo();

            if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
                BukkitRunnable teleportTask = teleportTasks.get(playerUUID);
                teleportTask.cancel();
                teleportTasks.remove(playerUUID);

                player.sendMessage(ChatColor.RED + "Teleportation canceled due to movement.");
            }
        }
    }
}
