package com.teqless.minewars.listeners;

import com.teqless.minewars.MineWars;
import com.teqless.minewars.game.GameHandler;
import com.teqless.minewars.game.GameState;
import com.teqless.minewars.teams.User;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        GameHandler handler = MineWars.getHandler();
        GameState state = handler.getState();
        User user = handler.getUser(player.getUniqueId());

        if(state == GameState.PRE_GAME &! user.isSpectating()) {
            if(handler.hasSpawnFreeze()) {
                assert to != null;
                player.teleport(new Location(player.getWorld(), from.getX(), from.getY(), from.getZ(), to.getYaw(),
                        to.getPitch()));
            }
        }

    }

}
