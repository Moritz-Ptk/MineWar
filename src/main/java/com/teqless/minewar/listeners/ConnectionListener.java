package com.teqless.minewar.listeners;

import com.teqless.minewar.MineWar;
import com.teqless.minewar.game.GameHandler;
import com.teqless.minewar.game.GameRules;
import com.teqless.minewar.game.GameState;
import com.teqless.minewar.items.InventoryHandler;
import com.teqless.minewar.location.TeleportManager;
import org.bukkit.GameMode;
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
        player.setGameMode(GameMode.ADVENTURE);

        GameHandler handler = MineWar.getHandler();
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

        GameHandler handler = MineWar.getHandler();
        GameState state = handler.getState();

        handler.removeUser(event.getPlayer().getUniqueId());

        if(state == GameState.LOBBY && handler.hasTaskRunning()
                && handler.getUsers().size() < GameRules.MIN_PLAYERS_TO_START) {
            handler.cancelTask();
        }

        event.setQuitMessage(null);
    }

}
