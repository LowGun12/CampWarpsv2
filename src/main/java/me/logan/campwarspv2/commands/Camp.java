package me.logan.campwarspv2.commands;

import me.logan.campwarspv2.Main;
import me.logan.campwarspv2.Utils.ColorUtils;
import me.logan.campwarspv2.Utils.ItemStackSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class Camp implements CommandExecutor {

    private Main plugin;
    private Inventory campGui;

    public Camp(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("camp").setExecutor(this);
    }

    public Inventory getOrCreateCampGui(Player player) {
        if (campGui == null) {
            campGui = createInv();
        }
        return campGui;
    }

    public Inventory createInv() {
        int size = plugin.getConfig().getInt("GuiSize");
        Inventory campGui = Bukkit.createInventory(null, size, ChatColor.GREEN.toString() + ChatColor.BOLD + plugin.getConfig().getString("GuiName"));

        // Load camp items from the configuration and add them to the inventory
        if (plugin.getConfig().contains("camps")) {
            for (String key : plugin.getConfig().getConfigurationSection("camps").getKeys(false)) {
                String path = "camps." + key;
                if (plugin.getConfig().contains(path + ".Item")) {
                    String serializedItem = plugin.getConfig().getString(path + ".Item");
                    ItemStack campItem = ItemStackSerializer.deserializeItemStack(serializedItem);
                    if (campItem != null) {
                        int slot = plugin.getConfig().getInt(path + ".Slot", 0);
                        campGui.setItem(slot, campItem);
                        plugin.getLogger().info("Added item to camp gui " + campItem.getType().toString());
                    }
                }
            }
        }

        return campGui;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Inventory campGui = getOrCreateCampGui(player);
            player.openInventory(campGui);
        } else {
            sender.sendMessage(ColorUtils.colorize("&aThis command can only be run by players"));
        }
        return true;
    }
}