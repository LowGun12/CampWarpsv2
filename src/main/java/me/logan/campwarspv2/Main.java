package me.logan.campwarspv2;

import me.logan.campwarspv2.Listeners.GuiListener;
import me.logan.campwarspv2.commands.Camp;
import me.logan.campwarspv2.commands.delCamp;
import me.logan.campwarspv2.commands.setCamp;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import me.logan.campwarspv2.Utils.ItemStackSerializer;

public final class Main extends JavaPlugin {

    private Camp camp;
    private Inventory campGui;

    @Override
    public void onEnable() {
        loadConfig();
        camp = new Camp(this);
        new setCamp(this);
        new delCamp(this);
        Bukkit.getPluginManager().registerEvents(new GuiListener(this), this);
        loadCampItems();

    }


    public Camp getGuiClass()
    {
        return camp;
    }


    private void loadCampItems() {
        this.getLogger().info("Loading camp items");
        campGui = camp.createInv();

        for (String key : getConfig().getKeys(false)) {
            if (getConfig().contains(key + ".Item")) {
                String serializedItem = getConfig().getString(key + ".Item");
                ItemStack campItem = ItemStackSerializer.deserializeItemStack(serializedItem);
                if (campItem != null) {
                    int slot = getConfig().getInt(key + ".Slot", 0);
                    campGui.setItem(slot, campItem);
                    this.getLogger().info("Added item to camp gui " + campItem.getType().toString());
                }
            }
        }
        this.getLogger().info("Camp items loaded successfully.");
    }







    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }




}