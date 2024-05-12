package com.teqless.minewars;

import com.teqless.minewars.game.GameHandler;
import com.teqless.minewars.listeners.handler.ListenerHandler;
import com.teqless.minewars.location.LocationManager;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class, handling basic Bukkit/Spigot functions
 */
public final class MineWars extends JavaPlugin {

    private static GameHandler handler;
    private static MineWars instance;

    @Override
    public void onEnable() {

        handler = new GameHandler();
        instance = this;

        ListenerHandler.registerEventListeners(getServer(), this);
        LocationManager.initFiles();
        LocationManager.loadLocationsFromConfig();
    }

    @Override
    public void onDisable() {
        LocationManager.saveConfig();
    }

    public static GameHandler getHandler() {
        return handler;
    }

    public static MineWars getInstance() {
        return instance;
    }

}
