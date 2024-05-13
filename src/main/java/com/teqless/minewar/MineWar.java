package com.teqless.minewar;

import com.teqless.minewar.command.CommandManager;
import com.teqless.minewar.game.GameHandler;
import com.teqless.minewar.listeners.handler.ListenerManager;
import com.teqless.minewar.location.LocationManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class, handling basic Bukkit/Spigot functions
 */
public final class MineWar extends JavaPlugin {

    private static GameHandler handler;
    private static MineWar instance;

    @Override
    public void onEnable() {

        handler = new GameHandler();
        instance = this;

        ListenerManager.registerEventListeners(getServer(), this);
        CommandManager.registerCommands(this);
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

    public static MineWar getInstance() {
        return instance;
    }

}
