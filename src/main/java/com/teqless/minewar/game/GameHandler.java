package com.teqless.minewar.game;

import com.teqless.minewar.MineWar;
import com.teqless.minewar.items.InventoryHandler;
import com.teqless.minewar.location.LocationManager;
import com.teqless.minewar.location.TeleportManager;
import com.teqless.minewar.teams.Team;
import com.teqless.minewar.teams.User;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GameHandler {
    private GameState state;
    private int currentCount;

    private final ArrayList<Team> teams;
    private final ArrayList<User> users;

    private BukkitTask task;

    private boolean spawnFreeze;
    private String winner;

    public GameHandler() {

        state = GameState.LOBBY;
        currentCount = 0;
        winner = "";

        users = new ArrayList<>();
        teams = new ArrayList<>();
        setupTeams();

        //Clean up worlds
        Bukkit.getServer().getWorlds().forEach(world -> {
            world.setTime(6000L);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_ENTITY_DROPS, false);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.SPAWN_RADIUS, 0);
            world.getEntities().forEach(Entity::remove);
        });

    }

    public void handleTeamElimination(Team team) {

        List<Team> teamsAlive = new ArrayList<>();
        teams.forEach(aliveTeam -> {
            if(aliveTeam.getLives() > 0 && aliveTeam.getSize() > 0) {
                teamsAlive.add(aliveTeam);
            } else {
                teamsAlive.remove(aliveTeam);
            }
        });

        if(teamsAlive.size() > 1) {

            if(team.getSize() > 0) {
                team.getMembers().forEach(user -> {
                    user.setSpectating();
                    Messages.sendMessage(user, Messages.OWN_TEAM_LOST_MESSAGE, false);
                });
            }

        } else {

            cancelTask();

            if(!teamsAlive.isEmpty()) {
                endGame(teamsAlive.get(0));
            } else {
                //Only supposed to happen in development scenario
                state = GameState.POST_GAME;
                Messages.broadcastMessage("Ended game without a winning team.");
            }

        }

    }

    private void endGame(Team winningTeam) {

        state = GameState.POST_GAME;
        winner = winningTeam.getName();

        Messages.broadcastMessage(Messages.TEAM_WIN_MESSAGE);

        users.forEach(user -> {
            if(user.isSpectating()) {
                user.removeSpectatorStatus();
            }
            TeleportManager.teleportToLobby(user);
            InventoryHandler.setPostGameInventory(user.getPlayer());
        });

        spawnVictoryFirework(winningTeam);

        BukkitRunnable runnable = new BukkitRunnable() {
            /**
             * Shutdown timer has to equal chest respawn time in order to allow all chests to regenerate before
             * the server stops
             */
            int timer = GameRules.CHEST_RESPAWN_TIME;
            @Override
            public void run() {

                users.forEach(user -> {
                    Player player = user.getPlayer();
                    player.setLevel(timer);
                    player.setExp((float) timer / GameRules.CHEST_RESPAWN_TIME);
                });

                //Stop server
                if (timer == 0 && task != null) {
                    task.cancel();
                    Bukkit.getServer().shutdown();
                    return;
                }

                currentCount = timer;
                if(timer < 5 || timer % 5 == 0) {
                    Messages.broadcastMessage(Messages.STOP_COUNTDOWN_MESSAGE);
                }
                timer--;
            }
        };
        task = runnable.runTaskTimer(MineWar.getInstance(), 0L, 20L);

    }

    private void startGame() {

        state = GameState.IN_GAME;
        Messages.broadcastMessage(Messages.MATCH_START_MESSAGE);

        users.forEach(user -> {
            Player player = user.getPlayer();
            player.setLevel(0);
            player.setExp(0);
            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        });

    }

    public void handleProtectionPhase() {

        Messages.broadcastMessage(Messages.PROTECTION_PHASE_START);
        spawnFreeze = false;

        BukkitRunnable runnable = new BukkitRunnable() {
            int timer = GameRules.PROTECTION_TIME;
            @Override
            public void run() {

                users.forEach(user -> {
                    Player player = user.getPlayer();
                    player.setLevel(timer);
                    player.setExp((float) timer / GameRules.PROTECTION_TIME);
                });

                //Start match
                if (timer == 0 && task != null) {
                    task.cancel();
                    startGame();
                    return;
                }

                currentCount = timer;
                if(timer < 5 || timer % 5 == 0) {
                    Messages.broadcastMessage(Messages.PROTECTION_COUNTDOWN_MESSAGE);
                }
                timer--;
            }
        };
        task = runnable.runTaskTimer(MineWar.getInstance(), 0L, 20L);

    }

    public void initiatePreGame() {

        state = GameState.PRE_GAME;

        distributeUsers();
        assignTeamLives();

        users.forEach(user -> {
            Player player = user.getPlayer();
            InventoryHandler.setGameInventory(player);
        });

        TeleportManager.teleportToSpawns(users);

        BukkitRunnable runnable = new BukkitRunnable() {
            int timer = GameRules.SPAWN_FREEZE_TIME;
            @Override
            public void run() {

                users.forEach(user -> {
                    Player player = user.getPlayer();
                    player.setLevel(timer);
                    player.setExp((float) timer / GameRules.SPAWN_FREEZE_TIME);
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                });

                //Start protection phase
                if (timer == 0 && task != null) {
                    task.cancel();
                    handleProtectionPhase();
                    return;
                }

                currentCount = timer;
                timer--;
            }
        };
        spawnFreeze = true;
        task = runnable.runTaskTimer(MineWar.getInstance(), 0L, 20L);

    }

    public void startLobbyCountdown() {

        BukkitRunnable runnable = new BukkitRunnable() {
            int timer = GameRules.LOBBY_COUNTDOWN_TIME;
            @Override
            public void run() {

                users.forEach(user -> {
                    Player player = user.getPlayer();
                    player.setLevel(timer);
                    player.setExp((float) timer / GameRules.LOBBY_COUNTDOWN_TIME);
                });

                //Kick off pre game
                if (timer == 0 && task != null) {
                    task.cancel();
                    initiatePreGame();
                    return;
                }

                currentCount = timer;
                if(timer < 5 || timer % 5 == 0) {
                    Messages.broadcastMessage(Messages.LOBBY_COUNTDOWN_MESSAGE);
                }
                timer--;
            }
        };
        task = runnable.runTaskTimer(MineWar.getInstance(), 0L, 20L);

    }

    private void spawnVictoryFirework(Team winningTeam) {

        Location location = LocationManager.getLocation("Lobby");
        Firework firework = Objects.requireNonNull(location.getWorld()).spawn(location, Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        Color color;

        switch (winningTeam.getName()) {

            case "Diamond":
                color = Color.fromRGB(185, 242, 255);
                break;
            case "Gold":
                color = Color.fromRGB(212, 175, 55);
                break;
            case "Emerald":
                color = Color.fromRGB(0, 208, 98);
                break;
            default:
                color = Color.RED;
        }

        fireworkMeta.addEffect(FireworkEffect.builder()
                .flicker(true)
                .trail(true)
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(color).build());

        fireworkMeta.setPower(1);
        firework.setFireworkMeta(fireworkMeta);
    }

    private void distributeUsers() {

        Random random = new Random();
        ArrayList<User> unassignedUsers = new ArrayList<>();

        users.forEach(user -> {
            if(user.getTeam() == null) {
                unassignedUsers.add(user);
            }
        });

        //Ensure certain size of team, dictated by amount of teams
        int desiredTeamSize = unassignedUsers.size() / GameRules.TEAMS.length;

        teams.forEach(team -> {
            while (team.getSize() < desiredTeamSize &! unassignedUsers.isEmpty()) {
                User user = unassignedUsers.get(random.nextInt(unassignedUsers.size()));
                team.addMember(user);
                unassignedUsers.remove(user);
            }
        });

        Team currentTeam = teams.get(random.nextInt(teams.size()));

        //Assign remaining players, going through teams, starting out at random team
        while (!unassignedUsers.isEmpty()) {

            User user = unassignedUsers.get(random.nextInt(unassignedUsers.size()));
            unassignedUsers.remove(user);
            currentTeam.addMember(user);

            if(teams.indexOf(currentTeam) == teams.size() - 1) currentTeam = teams.get(0);
            else currentTeam = teams.get(teams.indexOf(currentTeam) + 1);

        }

    }

    private void assignTeamLives() {

        AtomicInteger highestMemberCount = new AtomicInteger(0);

        teams.forEach(team -> {
            team.setLives(GameRules.BASE_TEAM_LIVES);
            if(team.getSize() > highestMemberCount.get()) highestMemberCount.set(team.getSize());
        });

        //Compensate for differences in team sizes by adding lives to smaller teams
        teams.forEach(team -> {
            if(team.getSize() < highestMemberCount.get()) {
                int dif = highestMemberCount.get() - team.getSize();
                int add = dif * 2;

                team.setLives(team.getLives() + add);
            }
        });

    }

    private void setupTeams() {
        Arrays.stream(GameRules.TEAMS).forEach(team -> teams.add(new Team(team)));
    }

    public GameState getState() {
        return state;
    }

    public void addUser(UUID uuid) {

        User user = new User(uuid);
        users.add(user);

        if(state == GameState.PRE_GAME || state == GameState.IN_GAME) {
            user.setSpectating();
        }
    }

    public void removeUser(UUID uuid) {

        User user = getUser(uuid);

        if(user.getTeam() != null) {
            user.getTeam().removeMember(user);
        }
        users.remove(getUser(uuid));
    }

    public User getUser(UUID uuid) {
        return users.stream().filter(user -> user.getUUID() == uuid).findAny().orElse(null);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void cancelTask() {
        if(task != null) {
            task.cancel();
            task = null;
        }
    }
    public boolean hasTaskRunning() {
        return task != null;
    }

    public boolean hasSpawnFreeze() {
        return spawnFreeze;
    }

    public String getWinner() {
        return winner;
    }

}
