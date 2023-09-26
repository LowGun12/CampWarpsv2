package me.logan.campwarpsv2.commands;

import me.logan.campwarpsv2.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CampReloadCommand implements CommandExecutor {

    private Main plugin;

    public CampReloadCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("campreload").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (!sender.hasPermission("yourplugin.reload")) {
                sender.sendMessage("You don't have permission to use this command.");
                return true;
            }
            plugin.reloadConfig();
            sender.sendMessage("Your plugin has been reloaded.");
        } else {
            sender.sendMessage("Only players can use this command.");
        }
        return true;
    }
}
