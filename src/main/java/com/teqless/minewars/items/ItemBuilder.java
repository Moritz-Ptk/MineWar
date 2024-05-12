package com.teqless.minewars.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Auxiliary Class for Item creation
 */
public class ItemBuilder {

    public static final Material HUB_ITEM_MATERIAL = Material.REDSTONE_BLOCK;
    public static final String HUB_ITEM_NAME = "§cBack to Hub";

    private static final Material teamSelectorMaterial = Material.ANVIL;
    private static final String teamSelectorName = "§7Team Selector";

    private static final Material spectatorItemMaterial = Material.COMPASS;
    private static final String spectatorItemName = "§7Spectator Tool";

    public static final Material SHOP_ITEM_MATERIAL = Material.BREWING_STAND;
    private static final String shopItemName = "§6Shop";

    private static final Material fillerItemMaterial = Material.LIGHT_GRAY_STAINED_GLASS_PANE;


    public static ItemStack createHubItem() {
        return ItemBuilder.createItem(HUB_ITEM_MATERIAL, 1, HUB_ITEM_NAME, true);
    }
    public static ItemStack createTeamSelectorItem() {
        return ItemBuilder.createItem(teamSelectorMaterial, 1, teamSelectorName, true);
    }

    public static ItemStack createSpectatorItem() {
        return ItemBuilder.createItem(spectatorItemMaterial, 1, spectatorItemName, true);
    }

    public static ItemStack createShopItem() {
        return ItemBuilder.createItem(SHOP_ITEM_MATERIAL, 1, shopItemName, true);
    }

    public static ItemStack createFillerItem() {
        return ItemBuilder.createItem(fillerItemMaterial, 1, "", true);
    }

    public static ItemStack createItem(Material type, int amount, String name, boolean unbreakable) {

        ItemStack itemStack = new ItemStack(type, amount);

        ItemMeta itemMeta = itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(name);
        itemMeta.setUnbreakable(unbreakable);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static ItemStack createPlayerHead(Player player, @Nullable ArrayList<String> desc) {

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        Objects.requireNonNull(meta).setOwningPlayer(player);
        head.setItemMeta(meta);

        if(desc != null) {
            setDescription(head, desc);
        }

        return head;
    }

    public static ItemStack setDescription(ItemStack itemStack, ArrayList<String> desc) {

        ItemMeta itemMeta = itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setLore(desc);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static void addEnchantment(ItemStack itemStack, Enchantment enchantment, int level) {
        itemStack.addEnchantment(enchantment, level);
    }

}
