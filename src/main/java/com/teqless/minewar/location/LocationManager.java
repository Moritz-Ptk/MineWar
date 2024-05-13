package com.teqless.minewar.location;

import com.teqless.minewar.MineWar;
import com.teqless.minewar.game.GameRules;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class LocationManager {

    private static HashMap<String, Location> locations = new HashMap<>();

    private static final File file = new File("plugins/MineWar", "locations.yml");
    private static final FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    private static final Location defaultLocation = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();

    public static void initFiles() {
        if(!file.exists()) {
            MineWar.getInstance().getDataFolder().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean saveLocation(String name, Location location) {

        config.set(name + ".world", location.getWorld().getName());
        config.set(name + ".x", location.getX());
        config.set(name + ".y", location.getY());
        config.set(name + ".z", location.getZ());
        config.set(name + ".yaw", location.getYaw());
        config.set(name + ".pitch", location.getPitch());

        loadLocationsFromConfig();
        return saveConfig();
    }

    public static Location getLocation(String name) {
        return locations.get(name);
    }

    private static Location getLocationFromConfig(String name) {

        Location location = defaultLocation;

        if(config.contains(name+ ".world")) {

            World world = Bukkit.getWorld(Objects.requireNonNull(config.getString(name + ".world")));

            double x = config.getDouble(name + ".x");
            double y = config.getDouble(name + ".y");
            double z = config.getDouble(name + ".z");

            double yaw = config.getDouble(name + ".yaw");
            double pitch = config.getDouble(name + ".pitch");

            location = new Location(world, x, y, z, (float) yaw, (float) pitch);
        }

        return location;
    }

    public static void loadLocationsFromConfig() {

        locations.clear();

        locations.put("Lobby", getLocationFromConfig("Lobby"));

        for(String team : GameRules.TEAMS) {
            locations.put(team, getLocationFromConfig(team));
        }

        for(int i = 0; i < GameRules.MAX_PLAYERS; i++) {
            locations.put("Spawn_" + i, getLocationFromConfig("Spawn_" + i));
        }

    }

    public static boolean saveConfig() {
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Resource intensive, don't call during regular game process
    public static int getSpawnAmount() {
        AtomicInteger spawns = new AtomicInteger(0);
        locations.keySet().forEach(location -> {
            if (location.contains("Spawn_")) {

                double x = locations.get(location).getX();
                double y = locations.get(location).getZ();
                double z = locations.get(location).getZ();

                if(x != defaultLocation.getX() && y != defaultLocation.getY() && z != defaultLocation.getZ()) {
                    spawns.getAndAdd(1);
                }
            }

        });
        return spawns.get();
    }

}
