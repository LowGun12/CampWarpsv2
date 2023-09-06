package me.logan.campwarspv2.commands;

import me.logan.campwarspv2.Main;
import me.logan.campwarspv2.Utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

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

            if (args.length < 1) {
                player.sendMessage(ColorUtils.colorize("&cUsage: /setcamp <name>"));
                return true;
            }

            String campName = ColorUtils.colorize(args[0]);
            String configCampName = campName.toLowerCase();
            ItemStack campItem = player.getInventory().getItemInMainHand().clone();
            if (campItem != null) {
                ItemMeta itemMeta = campItem.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(campName);
                    campItem.setItemMeta(itemMeta);

                    String serializedItem = serializeItemStack(campItem);
                    plugin.getConfig().set(configCampName + ".Item", serializedItem);

                    Camp camp = plugin.getGuiClass();
                    Inventory campGui = camp.getOrCreateCampGui(player); // Use the same instance
                    int slot = campGui.firstEmpty();

                    Location loc = player.getLocation();
                    plugin.getConfig().set(configCampName + ".World", loc.getWorld().getName());
                    plugin.getConfig().set(configCampName + ".X", loc.getX());
                    plugin.getConfig().set(configCampName + ".Y", loc.getY());
                    plugin.getConfig().set(configCampName + ".Z", loc.getZ());
                    plugin.getConfig().set(configCampName + ".Pitch", loc.getPitch());
                    plugin.getConfig().set(configCampName + ".Yaw", loc.getYaw());
                    plugin.getConfig().set(configCampName + ".Slot", slot);
                    campGui.setItem(slot, campItem);
                    plugin.saveConfig();

                    player.sendMessage(ColorUtils.colorize("&aCamp has been set"));
                }
            }
        }
        return true;
    }

    private String serializeItemStack(ItemStack itemStack) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = null;
        try {
            dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(itemStack);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (dataOutput != null) {
                    dataOutput.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
