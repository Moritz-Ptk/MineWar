package com.teqless.minewars.items;

import com.teqless.minewars.MineWars;
import com.teqless.minewars.game.GameRules;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GUIBuilder {

    private static final int spectatorGUISize = 27;

    public static void buildSpectatorGUI(Player player) {

        Inventory gui = Bukkit.createInventory(null, spectatorGUISize, "Players");
        AtomicInteger filled = new AtomicInteger();

        MineWars.getHandler().getUsers().forEach(user -> {

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
            for (int i = filled.get(); i < 27; i++) {
                gui.addItem(ItemBuilder.createFillerItem());
            }
        }

    }

}
