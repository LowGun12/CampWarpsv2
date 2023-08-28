package me.logan.campwarspv2;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setCamp implements CommandExecutor {

    private Main plugin;

    public setCamp(Main plguin) {
        this.plugin = plguin;
        plugin.getCommand("setcamp").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Cannot execute this command via console");
            return false;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("warp.setwarp")) {
            p.sendMessage(Color("&cNo permissions to run this command"));
            return false;
        }
        if (args.length == 0) {
            p.sendMessage(Color("&cInvalid use of this command. Correct usage /set warp {name}"));
            return false;
        }
        String name = args[0].toLowerCase();
        if (plugin.getConfig().get(name) != null) {
            p.sendMessage(Color("&cThis camp already exists"));
        }
        if
        Location loc = p.getLocation();
        plugin.getConfig().set(name + ".World", loc.getWorld().getName());
        plugin.getConfig().set(name + ".X", loc.getX());
        plugin.getConfig().set(name + ".Y", loc.getY());
        plugin.getConfig().set(name + ".Z", loc.getZ());
        plugin.getConfig().set(name + ".Pitch", loc.getPitch());
        plugin.getConfig().set(name + ".Yaw", loc.getYaw());
        plugin.saveConfig();
        p.sendMessage(Color("&a Camp has been set!"));


        return true;
    }
    private String Color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
