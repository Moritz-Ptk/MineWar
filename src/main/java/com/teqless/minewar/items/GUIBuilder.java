package com.teqless.minewar.items;

import com.teqless.minewar.MineWar;
import com.teqless.minewar.teams.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GUIBuilder {

    private static final int spectatorGUISize = 27;
    private static final int shopGUISize = 9;

    private static final String shopGUIName = "§6§lShop: §7[Your Points: §6§l%points§7]";

    public static void buildSpectatorGUI(Player player) {

        Inventory gui = Bukkit.createInventory(null, spectatorGUISize, "§a§lSpectator View");
        AtomicInteger filled = new AtomicInteger();

        MineWar.getHandler().getUsers().forEach(user -> {

            if(!user.isSpectating()) {
                ArrayList<String> desc = new ArrayList<>();
                desc.add("Team: " + user.getTeam());
                desc.add("Kills: " + user.getKills());

                ItemStack head = ItemBuilder.createPlayerHead(user.getPlayer(), desc);
                gui.addItem(head);
                filled.addAndGet(1);
            }

        });

        if(filled.get() < spectatorGUISize) {
            for (int i = filled.get(); i < spectatorGUISize; i++) {
                gui.setItem(i, ItemBuilder.createFillerItem());
            }
        }

        player.openInventory(gui);

    }

    public static void buildShopGUI(User user) {

        Player player = user.getPlayer();
        Inventory gui = Bukkit.createInventory(null, shopGUISize, shopGUIName.replace("%points",
                String.valueOf(user.getPoints())));
        AtomicInteger filled = new AtomicInteger();

        if(filled.get() < shopGUISize) {
            for (int i = filled.get(); i < shopGUISize; i++) {
                gui.setItem(i, ItemBuilder.createFillerItem());
            }
        }

        player.openInventory(gui);

    }

    public static void updateShopGUI(User user) {

        Player player = user.getPlayer();

        if(player.getOpenInventory().getTitle().contains("Shop")) {
            player.getOpenInventory().setTitle(shopGUIName.replace("%points", String.valueOf(user.getPoints())));
        }

    }

}
