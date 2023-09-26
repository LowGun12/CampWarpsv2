package me.logan.campwarpsv2.commands;

import me.logan.campwarpsv2.Main;
import me.logan.campwarpsv2.Utils.ColorUtils;
import me.logan.campwarpsv2.Utils.ItemStackSerializer;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class setCamp implements CommandExecutor {

    private Main plugin;

    public setCamp(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("setcamp").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!(player.hasPermission("camp.setwarp"))) {
                player.sendMessage(ColorUtils.colorize("&cNo permission for this"));
                return false;
            }

            if (args.length < 1) {
                player.sendMessage(ColorUtils.colorize("&cUsage: /setcamp <name>"));
                return false;
            }
            ItemStack mainHandCheck = player.getInventory().getItemInMainHand();
            if (mainHandCheck == null || mainHandCheck.getType().isAir()) {
                player.sendMessage(ColorUtils.colorize("&cYou must be holding an item in your main hand to use this command"));
            } else {

                String campName = args[0];
                String configCampName = campName.toLowerCase();
                ItemStack campItem = player.getInventory().getItemInMainHand().clone();
                if (campItem != null) {
                    ItemMeta itemMeta = campItem.getItemMeta();
                    if (itemMeta != null) {
                        itemMeta.setDisplayName(ColorUtils.colorize(campName));
                        itemMeta.setLore(Collections.singletonList(ColorUtils.colorize("&7&lClick to teleport to the continent of " + campName)));
                        campItem.setItemMeta(itemMeta);

                        // Save camp information under the "camps" section in config.yml
                        String path = "camps." + configCampName + ".";
                        plugin.getConfig().set(path + "Item", ItemStackSerializer.serializeItemStack(campItem));

                        Camp camp = plugin.getGuiClass();
                        Inventory campGui = camp.getOrCreateCampGui(player); // Use the same instance
                        int slot = campGui.firstEmpty();

                        Location loc = player.getLocation();
                        plugin.getConfig().set(path + "World", loc.getWorld().getName());
                        plugin.getConfig().set(path + "X", loc.getX());
                        plugin.getConfig().set(path + "Y", loc.getY());
                        plugin.getConfig().set(path + "Z", loc.getZ());
                        plugin.getConfig().set(path + "Pitch", loc.getPitch());
                        plugin.getConfig().set(path + "Yaw", loc.getYaw());
                        plugin.getConfig().set(path + "Slot", slot);
                        campGui.setItem(slot, campItem);
                        plugin.saveConfig();

                        player.sendMessage(ColorUtils.colorize("&aCamp has been set"));
                    }
                }
            }
        }
        return true;
    }
}
