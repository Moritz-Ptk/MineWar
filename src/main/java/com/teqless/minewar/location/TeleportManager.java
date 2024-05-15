package com.teqless.minewar.location;

import com.teqless.minewar.teams.Team;
import com.teqless.minewar.teams.User;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TeleportManager {

    public static void teleportToSpawns(List<User> users) {

        AtomicInteger spawnIndex = new AtomicInteger(0);

        users.forEach(user-> {

            Location spawn = LocationManager.getLocation("Spawn_" + spawnIndex.get());
            user.getPlayer().teleport(spawn);

            spawnIndex.getAndAdd(1);
        });

    }

    public static void teleportToLobby(Player player) {
        Location lobby = LocationManager.getLocation("Lobby");
        player.teleport(lobby);
    }

    public static void teleportToLobby(User user) {
        teleportToLobby(user.getPlayer());
    }

    public static Location getBase(User user) {
        Team team = user.getTeam();
        return LocationManager.getLocation(team.getName());
    }

    public static Location getSpawn() {
        return LocationManager.getLocation("Spawn_0");
    }

}
