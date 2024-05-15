package com.teqless.minewar.items;

import com.teqless.minewar.game.GameRules;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

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

    public static final Material SPECTATOR_ITEM_MATERIAL = Material.COMPASS;
    private static final String spectatorItemName = "§7Spectator Tool";

    public static final Material SHOP_ITEM_MATERIAL = Material.BREWING_STAND;
    private static final String shopItemName = "§6SHOP";

    private static final Material fillerItemMaterial = Material.GRAY_STAINED_GLASS_PANE;


    public static ItemStack createHubItem() {
        return ItemBuilder.createItem(HUB_ITEM_MATERIAL, 1, HUB_ITEM_NAME, true);
    }
    public static ItemStack createTeamSelectorItem() {
        return ItemBuilder.createItem(teamSelectorMaterial, 1, teamSelectorName, true);
    }

    public static ItemStack createSpectatorItem() {
        return ItemBuilder.createItem(SPECTATOR_ITEM_MATERIAL, 1, spectatorItemName, true);
    }

    public static ItemStack createShopItem() {
        return ItemBuilder.createItem(SHOP_ITEM_MATERIAL, 1, shopItemName, true);
    }

    public static ItemStack createFillerItem() {
        return ItemBuilder.createItem(fillerItemMaterial, 1, "§8§l[]", false);
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
        meta.setDisplayName("§7" + player.getName());
        head.setItemMeta(meta);

        if(desc != null) {
            setDescription(head, desc);
        }

        return head;
    }

    public static ItemStack createSplashPotion(PotionEffect effect, String name) {

        ItemStack potion = new ItemStack(Material.SPLASH_POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        meta.addCustomEffect(effect, true);
        meta.setDisplayName(name);
        potion.setItemMeta(meta);

        ArrayList<String> desc = new ArrayList<>();
        desc.add("§7Cost: §6" + GameRules.POTION_COST + " §7points");

        potion = setDescription(potion, desc);

        return potion;
    }

    public static ItemStack setName(ItemStack itemStack, String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(name);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
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
