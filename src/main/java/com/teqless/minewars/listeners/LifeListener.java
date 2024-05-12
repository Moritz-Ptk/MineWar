package com.teqless.minewars.listeners;

import com.teqless.minewars.MineWars;
import com.teqless.minewars.game.GameHandler;
import com.teqless.minewars.game.GameState;
import com.teqless.minewars.game.Messages;
import com.teqless.minewars.items.InventoryHandler;
import com.teqless.minewars.location.TeleportManager;
import com.teqless.minewars.teams.Team;
import com.teqless.minewars.teams.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class LifeListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();

        GameHandler handler = MineWars.getHandler();
        GameState state = handler.getState();
        User user = handler.getUser(player.getUniqueId());
        Team team = user.getTeam();

        Messages.sendMessage(user, Messages.DEATH_MESSAGE, false);
        team.reduceLives();

        if(player.getKiller() != null) {

            User killer = handler.getUser(player.getKiller().getUniqueId());
            Messages.sendMessage(killer, Messages.KILL_MESSAGE.replace("%victim", player.getName()),false);
            team.addKill(killer);

        }

        event.setDeathMessage(null);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {

        Player player = event.getPlayer();

        GameHandler handler = MineWars.getHandler();
        GameState state = handler.getState();
        User user = handler.getUser(player.getUniqueId());

        if(state == GameState.IN_GAME) {
            InventoryHandler.setGameInventory(player);
            TeleportManager.teleportToBase(user);
        } else {
            InventoryHandler.setPostGameInventory(player);
        }

    }

}
