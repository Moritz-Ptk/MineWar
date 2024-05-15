package com.teqless.minewar.listeners;

import com.teqless.minewar.MineWar;
import com.teqless.minewar.game.GameHandler;
import com.teqless.minewar.game.GameRules;
import com.teqless.minewar.game.GameState;
import com.teqless.minewar.game.Messages;
import com.teqless.minewar.items.ChestManager;
import com.teqless.minewar.items.GUIBuilder;
import com.teqless.minewar.items.ItemBuilder;
import com.teqless.minewar.teams.User;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Class for dealing with all Item related Events
 */
public class InteractListener implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();

        GameHandler handler = MineWar.getHandler();
        GameState state = handler.getState();
        User user = handler.getUser(player.getUniqueId());

        if(event.getCurrentItem() != null) {
            if(event.getCurrentItem().getType().equals(ItemBuilder.SHOP_ITEM_MATERIAL)) {
                event.setCancelled(true);
            }
        }

        if(inventory == player.getInventory()) {

            if(state == GameState.LOBBY || state == GameState.POST_GAME || user.isSpectating()) {
                event.setCancelled(true);
            }

        } else {

            String title = player.getOpenInventory().getTitle();

            if(title.contains("Shop")) {

                event.setCancelled(true);

                if(event.getCurrentItem() != null) {

                    Material type = event.getCurrentItem().getType();

                    if(type == Material.SPLASH_POTION) {

                        if(user.getPoints() >= GameRules.POTION_COST) {
                            player.getInventory().addItem(ItemBuilder.setDescription(event.getCurrentItem(), new ArrayList<>()));
                            user.removePoints(GameRules.POTION_COST);
                            Messages.sendMessage(user, Messages.POTION_PURCHASE, false);
                            player.closeInventory();
                        } else {
                            Messages.sendMessage(user, Messages.NOT_ENOUGH_POINTS, false);
                        }

                    }

                }

            }

            if(title.contains("Spectator")) {

                event.setCancelled(true);

                if(event.getCurrentItem() != null) {

                    if(event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {

                        SkullMeta meta = (SkullMeta) event.getCurrentItem().getItemMeta();

                        assert meta != null;
                        if(meta.getOwningPlayer() != null) {

                            OfflinePlayer teleportTarget = meta.getOwningPlayer();

                            if (teleportTarget.isOnline()) {
                                player.teleport(Objects.requireNonNull(teleportTarget.getLocation()));
                            }

                        }

                    }

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

        GameHandler handler = MineWar.getHandler();
        GameState state = handler.getState();
        User user = handler.getUser(player.getUniqueId());

        if(material.equals(ItemBuilder.HUB_ITEM_MATERIAL)) {
            //TODO: coordinate command functionality of Server core plugin
            player.performCommand("hub");
            event.setCancelled(true);
        }

        if(material.equals(ItemBuilder.SPECTATOR_ITEM_MATERIAL) && user.isSpectating()) {
            GUIBuilder.buildSpectatorGUI(player);
            event.setCancelled(true);
        }

        if(material.equals(ItemBuilder.SHOP_ITEM_MATERIAL)) {
            GUIBuilder.buildShopGUI(user);
            event.setCancelled(true);
        }

        if(state == GameState.LOBBY || state == GameState.POST_GAME) {
            event.setCancelled(true);
        }

        if(event.getClickedBlock() != null) {

            if(user.isSpectating() || state == GameState.LOBBY
                    || state == GameState.POST_GAME) return;

            Material blockMaterial = event.getClickedBlock().getType();

            switch (blockMaterial) {

                case CHEST:
                    ChestManager.openRegularChest(player, event.getClickedBlock().getLocation());
                    event.setCancelled(true);
                    break;
                case ENDER_CHEST:
                    event.setCancelled(true);
                    if(state == GameState.IN_GAME) {
                        ChestManager.openEnderChest(player, event.getClickedBlock().getLocation());
                    }
                    break;

            }

        }

    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {

        Player player = event.getPlayer();
        GameHandler handler = MineWar.getHandler();
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
        GameHandler handler = MineWar.getHandler();
        GameState state = handler.getState();

        if(state == GameState.LOBBY || state == GameState.POST_GAME
                || handler.getUser(player.getUniqueId()).isSpectating()) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onSwitchHands(PlayerSwapHandItemsEvent event) {

        Player player = event.getPlayer();
        GameHandler handler = MineWar.getHandler();
        GameState state = handler.getState();

        if(state == GameState.LOBBY || state == GameState.POST_GAME
                || handler.getUser(player.getUniqueId()).isSpectating()) {
            event.setCancelled(true);
        }

    }

}
