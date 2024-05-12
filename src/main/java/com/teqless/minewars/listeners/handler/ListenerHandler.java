package com.teqless.minewars.listeners.handler;

import com.teqless.minewars.listeners.*;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

/**
 * Listener Handler class, managing Spigot Event Listeners
 */
public class ListenerHandler {

    private static final ArrayList<Listener> eventListeners = new ArrayList<>();

    public static void registerEventListeners(Server server, Plugin plugin) {

        eventListeners.add(new FoodListener());
        eventListeners.add(new DamageListener());
        eventListeners.add(new ConnectionListener());
        eventListeners.add(new InteractListener());
        eventListeners.add(new MoveListener());
        eventListeners.add(new LifeListener());

        eventListeners.forEach(listener -> server.getPluginManager().registerEvents(listener, plugin));

    }

}
