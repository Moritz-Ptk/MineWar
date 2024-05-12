package com.teqless.minewars.items;

import com.teqless.minewars.MineWars;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class InventoryHandler {

    public static void equipOnJoin(Player player) {

        switch (MineWars.getHandler().getState()) {

            case LOBBY:
                setLobbyInventory(player);
                break;
            case PRE_GAME:
            case IN_GAME:
                setSpectatorInventory(player);
                break;
            case POST_GAME:
                setPostGameInventory(player);
                break;

        }

    }

    public static void setGameInventory(Player player) {

        PlayerInventory inv = player.getInventory();

        inv.clear();
        inv.setItem(0, ItemBuilder.createItem(Material.GOLDEN_SWORD, 1, "Sword", true));
        inv.setItem(8, ItemBuilder.createShopItem());

    }

    private static void setLobbyInventory(Player player) {

        PlayerInventory inv = player.getInventory();

        inv.clear();
        //inv.setItem(0, ItemBuilder.createTeamSelectorItem());
        inv.setItem(7, ItemBuilder.createItem(Material.BOOK, 1, "How to Play", true));
        inv.setItem(8, ItemBuilder.createHubItem());

    }

    public static void setSpectatorInventory(Player player) {

        PlayerInventory inv = player.getInventory();

        inv.clear();
        inv.setItem(0, ItemBuilder.createSpectatorItem());
        inv.setItem(8, ItemBuilder.createHubItem());

    }

    public static void setPostGameInventory(Player player) {

        PlayerInventory inv = player.getInventory();

        inv.clear();
        inv.setItem(8, ItemBuilder.createHubItem());

    }

}
