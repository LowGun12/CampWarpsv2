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
        Inventory campGui = Bukkit.createInventory(null, size, ChatColor.GREEN.toString() + ChatColor.BOLD + "Camps");

        // Load camp items from the configuration and add them to the inventory
        for (String key : plugin.getConfig().getKeys(false)) {
            if (key.endsWith(".Item")) {
                String serializedItem = plugin.getConfig().getString(key);
                ItemStack campItem = ItemStackSerializer.deserializeItemStack(serializedItem);
                if (campItem != null) {
                    campGui.addItem(campItem);
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