package com.teqless.minewars.listeners;

import com.teqless.minewars.MineWars;
import com.teqless.minewars.game.GameHandler;
import com.teqless.minewars.game.GameRules;
import com.teqless.minewars.game.GameState;
import com.teqless.minewars.items.InventoryHandler;
import com.teqless.minewars.location.TeleportManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener Class for dealing with Player connections
 */
public class ConnectionListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        //TODO: Teleport to proper spawn

        //Ensuring cleaned up Player properties
        player.setHealth(20);
        player.setLevel(0);
        player.setExp(0);
        player.setFoodLevel(20);
        player.setInvisible(false);

        GameHandler handler = MineWars.getHandler();
        GameState state = handler.getState();

        handler.addUser(player.getUniqueId());
        InventoryHandler.equipOnJoin(player);
        TeleportManager.teleportToLobby(player);

        if(state == GameState.PRE_GAME || state == GameState.IN_GAME) {
            player.setAllowFlight(true);
            player.setFlying(true);
        } else {
            player.setAllowFlight(false);
            player.setFlying(false);
        }

        if(state == GameState.LOBBY) {
            if(handler.getUsers().size() >= GameRules.MIN_PLAYERS_TO_START &! handler.hasTaskRunning()) {
                handler.startLobbyCountdown();
            }
        }

        event.setJoinMessage(null);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {

        GameHandler handler = MineWars.getHandler();
        GameState state = handler.getState();

        handler.removeUser(event.getPlayer().getUniqueId());

        if(state == GameState.LOBBY && handler.hasTaskRunning()
                && handler.getUsers().size() < GameRules.MIN_PLAYERS_TO_START) {
            handler.cancelTask();
        }

        event.setQuitMessage(null);
    }

}
