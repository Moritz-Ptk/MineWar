package com.teqless.minewar.items;

import com.teqless.minewar.MineWar;
import com.teqless.minewar.game.GameRules;
import com.teqless.minewar.teams.User;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ChestManager {

    public static void openRegularChest(Player player, Location location) {

        location.getBlock().setType(Material.AIR);

        PlayerInventory inventory = player.getInventory();
        ArrayList<Material> possibleLoot = new ArrayList<>(Arrays.asList(GameRules.CHEST_LOOT));
        //Avoid duplications
        Arrays.asList(inventory.getContents()).forEach(itemStack -> {
            if(itemStack != null) {
                Material material = itemStack.getType();
                possibleLoot.remove(material);
            }
        });

        possibleLoot.add(Material.ARROW);
        Random random = new Random();

        ItemStack loot = ItemBuilder.createItem(possibleLoot.get(random.nextInt(possibleLoot.size())), 1, "", true);
        String itemName = "§7" + loot.getType().name().replace("_", " ").toLowerCase().toUpperCase(Locale.ROOT);
        loot = ItemBuilder.setName(loot, itemName);

        if(loot.getType().equals(Material.ARROW)) {
            loot.setAmount(4);
        }

        inventory.addItem(loot);
        
        //Respawning the Chest after 30 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                location.getBlock().setType(Material.CHEST);
            }
        }.runTaskLater(MineWar.getInstance(), 20L * GameRules.CHEST_RESPAWN_TIME);

    }
    
    public static void openEnderChest(Player player, Location location) {

        location.getBlock().setType(Material.AIR);

        PlayerInventory inventory = player.getInventory();
        ArrayList<Material> possibleLoot = new ArrayList<>(Arrays.asList(GameRules.ENDER_CHEST_LOOT));
        //Avoid duplications
        Arrays.asList(inventory.getContents()).forEach(itemStack -> {
            if(itemStack != null) {
                Material material = itemStack.getType();
                possibleLoot.remove(material);
            }
        });

        Random random = new Random();

        if(random.nextInt(10) <= 2) {

            //Handle "bad luck" explosion

            player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, location, 3);
            player.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);

            User user = MineWar.getHandler().getUser(player.getUniqueId());
            user.getTeam().getMembers().forEach(mate -> mate.addPoints(1));

            player.getWorld().getNearbyEntities(location, 4, 4, 4).forEach(entity -> {
                if(entity instanceof LivingEntity) {

                    LivingEntity livingEntity = (LivingEntity) entity;
                    double health = livingEntity.getHealth();
                    double baseDamage = 8;

                    //Prevent death from explosion, might cause unchecked disturbance with kill system otherwise
                    if(health < baseDamage) {
                        livingEntity.damage(health - 1);
                    } else {
                        livingEntity.damage(baseDamage);
                    }

                }
            });

        } else {

            //Handle "good luck" loot creation

            ItemStack loot = ItemBuilder.createItem(possibleLoot.get(random.nextInt(possibleLoot.size())), 1, "", true);
            String itemName = "§b" + loot.getType().name().replace("_", " ").toLowerCase().toUpperCase(Locale.ROOT);
            loot = ItemBuilder.setName(loot, itemName);

            inventory.addItem(loot);


        }
        new BukkitRunnable() {
            @Override
            public void run() {
                location.getBlock().setType(Material.ENDER_CHEST);
            }
        }.runTaskLater(MineWar.getInstance(), 20L * GameRules.ENDER_CHEST_RESPAWN_TIME);
    }

}
