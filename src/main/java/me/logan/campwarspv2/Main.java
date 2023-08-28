package me.logan.campwarspv2;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        loadConfig();


    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }


}
