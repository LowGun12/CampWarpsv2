package me.logan.campwarpsv2.commands;

import me.logan.campwarpsv2.Main;
import me.logan.campwarpsv2.Utils.ColorUtils;
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

        Player player = (Player) sender;
        if (!player.hasPermission("camp.delwarp")) {
            player.sendMessage(ColorUtils.colorize("&cNo permission for this"));
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(ColorUtils.colorize("&cIncorrect usage, please provide a name"));
            return false;
        }

        String name = args[0].toLowerCase();
        String campPath = "camps." + name;
        if (plugin.getConfig().get(campPath) == null) {
            player.sendMessage(ColorUtils.colorize("&cThis warp doesn't exist"));
            return false;
        }

        // Remove the item from the /camp GUI
        Inventory campGui = plugin.getGuiClass().getOrCreateCampGui(player);
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
        plugin.getConfig().set(campPath, null);
        plugin.saveConfig();

        player.sendMessage(ColorUtils.colorize("&aWarp " + name + " deleted"));
        return true;
    }
}

