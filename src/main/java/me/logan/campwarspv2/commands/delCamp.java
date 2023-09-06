package me.logan.campwarspv2.commands;

import me.logan.campwarspv2.Main;
import me.logan.campwarspv2.Utils.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class delCamp implements CommandExecutor {


    private Main plugin;

    public delCamp(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("delcamp").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Player only command");
            return false;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("camp.delwarp")) {
            p.sendMessage(ColorUtils.colorize("&cNo permission for this"));
            return false;
        }

        if (args.length == 0) {
            p.sendMessage(ColorUtils.colorize("&cIncorrect usage, please provide a name"));
            return false;
        }

        String name = args[0].toLowerCase();
        if (plugin.getConfig().get(name) == null) {
            p.sendMessage(ColorUtils.colorize("&cThis warp doesn't exist"));
            return false;
        }

        // Remove the item from the /camp GUI
        Inventory campGui = plugin.getGuiClass().getOrCreateCampGui(p);
        ItemStack[] contents = campGui.getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                String displayName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
                if (displayName.equalsIgnoreCase(name)) {
                    contents[i] = null; // Remove the item
                    campGui.setContents(contents);
                    break; // We found and removed the item, so exit the loop
                }
            }
        }

        // Remove the camp from the configuration
        plugin.getConfig().set(name, null);
        plugin.saveConfig();

        p.sendMessage(ColorUtils.colorize("&aWarp " + name + " deleted"));
        return true;
    }
}

