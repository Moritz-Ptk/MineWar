package com.teqless.minewars.items;

import com.teqless.minewars.MineWars;
import com.teqless.minewars.game.GameRules;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class ChestManager {

    public static void openRegularChest(Player player, Location location) {

        location.getBlock().setType(Material.AIR);

        //Respawning the Chest after 30 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                location.getBlock().setType(Material.CHEST);
            }
        }.runTaskLater(MineWars.getInstance(), 20L * GameRules.CHEST_RESPAWN_TIME);

    }

}
