package com.teqless.minewar.command.commands;

import com.teqless.minewar.MineWar;
import com.teqless.minewar.game.GameRules;
import com.teqless.minewar.game.Messages;
import com.teqless.minewar.location.LocationManager;
import com.teqless.minewar.teams.User;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class LocationCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;
            Location location =  player.getLocation();
            User user = MineWar.getHandler().getUser(player.getUniqueId());

            //TODO: Introduce actual permission
            if(player.isOp()) {

                if(args.length == 0) {
                    Messages.sendMessage(user, "§7Correct usage: /locations [§cadd§7 | reload | help] " +
                                    "{§clocation_name§7}",
                            false);
                    return false;
                }

                switch (args[0].toLowerCase()) {

                    case "add":

                        StringBuilder addError = new StringBuilder("§cSyntax Error. §7Correct usage: /locations " +
                                "add [Lobby | Spawn");
                        Arrays.asList(GameRules.TEAMS).forEach(team -> addError.append(" §7| ").append(team));
                        addError.append("§7]");

                        String added = "§7Added location for %loc";

                        if(args.length != 2) {
                            Messages.sendMessage(user, addError.toString(), false);
                        } else {

                            String locationName =
                                    args[1].toLowerCase().substring(0, 1).toUpperCase()
                                            + args[1].toLowerCase().substring(1);

                            switch (locationName) {

                                case "Lobby":
                                    LocationManager.saveLocation("Lobby", location);
                                    Messages.sendMessage(user, added.replace("%loc", locationName), false);
                                    break;

                                case "Spawn":
                                    LocationManager.saveLocation("Spawn_" + LocationManager.getSpawnAmount(), location);
                                    Messages.sendMessage(user, added.replace("%loc", locationName), false);
                                    break;

                                default:

                                    if(Arrays.asList(GameRules.TEAMS).contains(locationName)) {
                                        LocationManager.saveLocation(locationName, location);
                                        Messages.sendMessage(user, added.replace("%loc", locationName), false);
                                    } else {
                                        Messages.sendMessage(user, addError.toString(), false);
                                    }

                            }

                        }
                        break;

                    case "help":
                        Messages.sendMessage(user, "§7Correct usage: /locations [§cadd§7 | reload | help] " +
                                        "{§clocation_name§7}",
                                false);
                        break;

                    case "reload":
                        LocationManager.initFiles();
                        LocationManager.saveConfig();
                        LocationManager.loadLocationsFromConfig();
                        Messages.sendMessage(user, "§7Reloaded locations from configuration file.", false);
                        break;

                    default:
                        Messages.sendMessage(user, Messages.SYNTAX_ERROR, false);
                        break;

                }

            } else {
                Messages.sendMessage(user, Messages.NO_PERMISSION, false);
            }

        }

        return false;
    }

}
