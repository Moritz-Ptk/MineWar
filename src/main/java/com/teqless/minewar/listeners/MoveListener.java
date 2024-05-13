package com.teqless.minewar.listeners;

import com.teqless.minewar.MineWar;
import com.teqless.minewar.game.GameHandler;
import com.teqless.minewar.game.GameState;
import com.teqless.minewar.teams.User;
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

        GameHandler handler = MineWar.getHandler();
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
