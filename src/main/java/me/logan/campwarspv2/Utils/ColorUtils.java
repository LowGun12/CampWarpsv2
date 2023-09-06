package me.logan.campwarspv2.Utils;

import org.bukkit.ChatColor;

public class ColorUtils {


    public static String colorize(String s) {

            return ChatColor.translateAlternateColorCodes('&', s);
    }
}
