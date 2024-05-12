package com.teqless.minewars.game;

import com.teqless.minewars.MineWars;
import com.teqless.minewars.teams.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Messages {

    private static final String prefix = "§cMine§8§lWars §7» ";

    //Accent color, abbreviated for format
    private static final String aC = "§c";
    //Reset code, abbreviated for format
    private static final String r = "§7";

    public static final String KILL_STREAK_MESSAGE = "You are on a %kills kill killstreak!";
    public static final String LOBBY_COUNTDOWN_MESSAGE = "The match starts in %seconds seconds.";
    public static final String STOP_COUNTDOWN_MESSAGE = "The server stops in %seconds seconds.";

    public static final String PROTECTION_COUNTDOWN_MESSAGE = "The combat phase starts in %seconds seconds.";
    public static final String MATCH_START_MESSAGE = "The match has started! Good luck and have fun!";
    public static final String PROTECTION_PHASE_START =
            "The pre-game phase has started! You now have "+ aC + GameRules.PROTECTION_TIME + r + " seconds to find " +
        "loot and prepare yourself for the match.";
    public static final String JOIN_TEAM_MESSAGE = "You are now in team %team.";
    public static final String OWN_TEAM_LOST_MESSAGE = "Your team has been eliminated, you will now be spectating for" +
            " the rest of the game.";

    public static final String TEAM_NO_LIVES_LEFT_MESSAGE = "Team %team" + r + " has been eliminated! They had no " +
            "remaining lives.";

    public static final String TEAM_NO_MEMBERS_LEFT_MESSAGE = "Team %team" + r + " has been eliminated! They had no " +
            "remaining players.";
    public static final String TEAM_WIN_MESSAGE = "Team %winner has won the game! Congratulations!";
    public static final String KILL_MESSAGE = "You killed "+ aC + "%victim" + r + ".";
    public static final String DEATH_MESSAGE = "You have been killed by %killer.";
    public static final String RECEIVE_POINTS_MESSAGE = "You received "+ aC + "%points" + r + " points.";

    public static void sendMessage(User user, String message, boolean broadcast) {

        Player player = user.getPlayer();

        //Replace placeholders
        message = prefix + message
                .replace("%team", user.getTeam().getName() + r)
                .replace("%player", aC + player.getName()+ r)
                .replace("%kills", aC + user.getKills()+ r)
                .replace("%seconds", aC + MineWars.getHandler().getCurrentCount() + r)
                .replace("%winner", aC + MineWars.getHandler().getWinner() + r
                );

        if(player.getKiller() != null) {
            message = message.replace("%killer", aC + player.getKiller().getName() + r);
        }

        if(MineWars.getHandler().getCurrentCount() == 1) {
            message = message.replace("seconds", "second");
        }

        message = message
                .replace("Emerald", "§aEmerald")
                .replace("Diamond", "§bDiamond")
                .replace("Gold", "§6Gold"
                );

        if(broadcast) {
            Bukkit.broadcastMessage(message);
        } else {
            player.sendMessage(message);
        }

    }

    public static void broadcastMessage(String message) {

        //Replace placeholders
        message = prefix + message
                .replace("%seconds", aC + MineWars.getHandler().getCurrentCount() + r)
                .replace("%winner", aC + MineWars.getHandler().getWinner() + r
                );


        //Non-elegant solution to fix one PROTECTION_PHASE message
        if(MineWars.getHandler().getCurrentCount() == 1 &! message.contains("prepare")) {
            message = message.replace("seconds", "second");
        }

        message = message
                .replace("Emerald", "§aEmerald")
                .replace("Diamond", "§bDiamond")
                .replace("Gold", "§6Gold"
                );

        Bukkit.broadcastMessage(message);
    }

}
