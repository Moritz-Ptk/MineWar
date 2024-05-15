package com.teqless.minewar.teams;

import com.teqless.minewar.MineWar;
import com.teqless.minewar.game.Messages;
import com.teqless.minewar.items.ItemBuilder;
import com.teqless.minewar.game.GameHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class User {

    private final UUID uuid;

    private int kills;
    private int points;
    private int killstreak;

    private boolean isSpectating;

    private Team team;

    public User(UUID uuid) {
        this.uuid = uuid;
        this.points = 0;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public UUID getUUID() {
        return uuid;
    }

    public int addKill() {

        kills += 1;
        killstreak += 1;

        Player player = getPlayer();
        PlayerInventory inventory = player.getInventory();

        player.setLevel(killstreak);

        switch (killstreak) {
            case 5:
                inventory.addItem(ItemBuilder.createItem(Material.IRON_SWORD,
                        1, "§7IRON SWORD - 5 Kills", true));
                break;
            case 7:
                Messages.sendMessage(this, Messages.KILL_STREAK_MESSAGE, true);

                inventory.setChestplate(ItemBuilder.createItem(Material.IRON_CHESTPLATE,
                        1, "§7IRON CHESTPLATE - 7 Kills", true));
                break;
            case 10:
                Messages.sendMessage(this, Messages.KILL_STREAK_MESSAGE, true);
                inventory.addItem(ItemBuilder.createItem(Material.DIAMOND_SWORD,
                        1, "§bDIAMOND SWORD - 10 Kills", true));
                break;
        }

        return kills;
    }

    public int getKills() {
        return kills;
    }

    public void setSpectating() {

        isSpectating = true;

        GameHandler handler = MineWar.getHandler();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if(!handler.getUser(player.getUniqueId()).isSpectating()) {
                player.hidePlayer(MineWar.getInstance(), getPlayer());
            }
        }

    }

    public void removeSpectatorStatus() {

        isSpectating = false;

        Player player = getPlayer();

        player.setFlying(false);
        player.setAllowFlight(false);

        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            otherPlayer.showPlayer(MineWar.getInstance(), player);
        }

    }

    public boolean isSpectating() {
        return isSpectating;
    }

    public void setTeam(Team team) {
        this.team = team;
        if(team != null) {
            Messages.sendMessage(this, Messages.JOIN_TEAM_MESSAGE, false);
        }
    }

    public Team getTeam() {
        return team;
    }

    public void addPoints(int points) {

        if(points > 1) {
            Messages.sendMessage(this, Messages.RECEIVE_POINTS_MESSAGE.replace("%points",
                    String.valueOf(points)), false);
        }

        this.points += points;
    }

    public void removePoints(int points) {
        this.points -= points;
    }

    public int getPoints() {
        return points;
    }

    public int getKillstreak() {
        return killstreak;
    }

    public void setKillstreak(int killstreak) {
        this.killstreak = killstreak;
    }

}
