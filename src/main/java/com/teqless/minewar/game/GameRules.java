package com.teqless.minewar.game;

import org.bukkit.Material;

public final class GameRules {

    public static boolean FALL_DAMAGE = false;
    public static String[] TEAMS = {"Diamond", "Gold", "Emerald"};
    public static int CHEST_RESPAWN_TIME = 30;
    public static int ENDER_CHEST_RESPAWN_TIME = 2 * 60;
    public static int MIN_PLAYERS_TO_START = 2;
    public static int MAX_PLAYERS = 24;
    public static int BASE_TEAM_LIVES = 5;
    public static int LOBBY_COUNTDOWN_TIME = 15;
    public static int SPAWN_FREEZE_TIME = 5;
    public static int PROTECTION_TIME = 30;
    public static Material[] CHEST_LOOT = {Material.LEATHER_CHESTPLATE, Material.BOW, Material.STONE_SWORD};
    public static Material[] ENDER_CHEST_LOOT = {Material.IRON_SWORD, Material.BOW, Material.IRON_CHESTPLATE};
    public static int POTION_COST = 15;

}
