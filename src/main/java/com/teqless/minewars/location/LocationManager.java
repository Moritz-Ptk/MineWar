package com.teqless.minewars.location;

import com.teqless.minewars.game.GameRules;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class LocationManager {

    private static HashMap<String, Location> locations = new HashMap<>();

    private static final File file = new File("plugins/MineWars", "locations.yml");
    private static final FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    public static void initFiles() {
        if(!file.exists()) {
            file.mkdirs();
        }
    }

    public static boolean saveLocation(String name, Location location) {

        config.set(name + ".world", location.getWorld());
        config.set(name + ".x", location.getX());
        config.set(name + ".y", location.getY());
        config.set(name + ".z", location.getZ());
        config.set(name + ".yaw", location.getYaw());
        config.set(name + ".pitch", location.getPitch());

        return saveConfig();
    }

    public static Location getLocation(String name) {
        return locations.get(name);
    }

    private static Location getLocationFromConfig(String name) {

        Location location = null;

        if(config.contains(name+ ".world")) {

            World world = Bukkit.getWorld(Objects.requireNonNull(config.getString(name + ".world")));

            double x = config.getDouble(name + ".x");
            double y = config.getDouble(name + ".x");
            double z = config.getDouble(name + ".x");

            double yaw = config.getDouble(name + ".yaw");
            double pitch = config.getDouble(name + ".pitch");

            location = new Location(world, x, y, z, (float) yaw, (float) pitch);
        }

        return location;
    }

    public static void loadLocationsFromConfig() {

        Location defaultLocation = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();

        if(getLocationFromConfig("Lobby") != null) {
            locations.put("Lobby", getLocationFromConfig("Lobby"));
        } else {
            locations.put("Lobby", defaultLocation);
        }

        for(String team : GameRules.TEAMS) {
            if(getLocationFromConfig(team) != null) {
                locations.put(team, getLocationFromConfig(team));
            } else {
                locations.put(team, defaultLocation);
            }
        }

        for(int i = 0; i < GameRules.MAX_PLAYERS; i++) {
            if(getLocationFromConfig("Spawn_" + i) != null) {
                locations.put("Spawn_" + i, getLocationFromConfig("Spawn_" + i));
            } else {
                locations.put("Spawn_" + i, defaultLocation);
            }
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

}
