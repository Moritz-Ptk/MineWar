package com.teqless.minewar.listeners;

import com.teqless.minewar.MineWar;
import com.teqless.minewar.game.GameHandler;
import com.teqless.minewar.game.GameState;
import com.teqless.minewar.game.Messages;
import com.teqless.minewar.items.InventoryHandler;
import com.teqless.minewar.location.TeleportManager;
import com.teqless.minewar.teams.Team;
import com.teqless.minewar.teams.User;
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

        GameHandler handler = MineWar.getHandler();
        User user = handler.getUser(player.getUniqueId());
        Team team = user.getTeam();

        user.setKillstreak(0);
        player.setLevel(0);

        Messages.sendMessage(user, Messages.DEATH_MESSAGE, false);
        team.reduceLives();

        if(player.getKiller() != null) {

            User killer = handler.getUser(player.getKiller().getUniqueId());
            Messages.sendMessage(killer, Messages.KILL_MESSAGE.replace("%victim", player.getName()),false);
            team.addKill(killer);

        }

        event.getDrops().clear();
        event.setDroppedExp(0);
        event.setDeathMessage(null);

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {

        Player player = event.getPlayer();

        GameHandler handler = MineWar.getHandler();
        GameState state = handler.getState();
        User user = handler.getUser(player.getUniqueId());

        if(state == GameState.IN_GAME) {
            if(!user.isSpectating()) {
                InventoryHandler.setGameInventory(player);
                event.setRespawnLocation(TeleportManager.getBase(user));
            } else {
                InventoryHandler.setSpectatorInventory(player);
                event.setRespawnLocation(TeleportManager.getSpawn());
            }
        } else {
            TeleportManager.teleportToLobby(player);
            InventoryHandler.setPostGameInventory(player);
        }

    }

}
