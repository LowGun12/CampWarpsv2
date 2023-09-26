package me.logan.campwarpsv2;

import me.logan.campwarpsv2.Listeners.GuiListener;
import me.logan.campwarpsv2.commands.Camp;
import me.logan.campwarpsv2.commands.delCamp;
import me.logan.campwarpsv2.commands.setCamp;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private Camp camp;
    private Inventory campGui;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        camp = new Camp(this);
        new setCamp(this);
        new delCamp(this);
        Bukkit.getPluginManager().registerEvents(new GuiListener(this), this);


    }

    public Camp getGuiClass()
    {
        return camp;
    }


}