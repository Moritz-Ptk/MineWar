package com.teqless.minewar.command;

import com.teqless.minewar.command.commands.LocationCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager {

    public static void registerCommands(JavaPlugin plugin) {
        plugin.getCommand("locations").setExecutor(new LocationCommand());
    }

}
