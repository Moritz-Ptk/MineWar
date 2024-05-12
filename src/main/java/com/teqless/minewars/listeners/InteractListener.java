package com.teqless.minewars.listeners;

import com.teqless.minewars.MineWars;
import com.teqless.minewars.game.GameHandler;
import com.teqless.minewars.game.GameState;
import com.teqless.minewars.items.ChestManager;
import com.teqless.minewars.items.ItemBuilder;
import com.teqless.minewars.teams.User;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Class for dealing with all Item related Events
 */
public class InteractListener implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();

        GameHandler handler = MineWars.getHandler();
        GameState state = handler.getState();
        User user = handler.getUser(player.getUniqueId());


        if(inventory == player.getInventory()) {
            if(state == GameState.LOBBY || state == GameState.POST_GAME || user.isSpectating()) {
                event.setCancelled(true);
            }
            if(event.getCursor() != null) {
                if(event.getCursor().getType().equals(ItemBuilder.SHOP_ITEM_MATERIAL)) {
                    event.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack inHand = inventory.getItemInMainHand();
        Material material = inHand.getType();

        GameHandler handler = MineWars.getHandler();
        GameState state = handler.getState();

        if(material.equals(ItemBuilder.HUB_ITEM_MATERIAL)) {
            //TODO: coordinate command functionality of Server core plugin
            player.performCommand("hub");
        }

        if(state == GameState.LOBBY || state == GameState.POST_GAME
                || material.equals(ItemBuilder.SHOP_ITEM_MATERIAL)) {
            event.setCancelled(true);
        }

        if(event.getClickedBlock() != null) {

            event.setCancelled(true);
            if(MineWars.getHandler().getUser(player.getUniqueId()).isSpectating()) return;

            Material blockMaterial = event.getClickedBlock().getType();

            switch (blockMaterial) {

                case CHEST:
                    ChestManager.openRegularChest(player, event.getClickedBlock().getLocation());
                    break;
                case ENDER_CHEST:
                    //TODO: implement ender chest
                    ChestManager.openRegularChest(player, event.getClickedBlock().getLocation());
                    break;

            }

        }

        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

        }

    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {

        Player player = event.getPlayer();
        GameHandler handler = MineWars.getHandler();
        GameState state = handler.getState();

        if(state == GameState.LOBBY || state == GameState.POST_GAME
                || handler.getUser(player.getUniqueId()).isSpectating()
                || event.getItemDrop().getItemStack().getType().equals(ItemBuilder.SHOP_ITEM_MATERIAL)) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {

        Player player = event.getPlayer();
        GameHandler handler = MineWars.getHandler();
        GameState state = handler.getState();

        if(state == GameState.LOBBY || state == GameState.POST_GAME
                || handler.getUser(player.getUniqueId()).isSpectating()) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onSwitchHands(PlayerSwapHandItemsEvent event) {

        Player player = event.getPlayer();
        GameHandler handler = MineWars.getHandler();
        GameState state = handler.getState();

        if(state == GameState.LOBBY || state == GameState.POST_GAME
                || handler.getUser(player.getUniqueId()).isSpectating()) {
            event.setCancelled(true);
        }

    }

}
