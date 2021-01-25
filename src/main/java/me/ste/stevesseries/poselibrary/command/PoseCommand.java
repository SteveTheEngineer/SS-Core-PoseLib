package me.ste.stevesseries.poselibrary.command;

import me.ste.stevesseries.poselibrary.PoseLibrary;
import me.ste.stevesseries.poselibrary.PoseManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.util.*;

public class PoseCommand implements TabExecutor {
    public static final Map<NamespacedKey, PoseFactory> REGISTERED_POSES = new HashMap<>();
    public static final Map<String, NamespacedKey> LOOKUP_MAP = new HashMap<>();

    private final PoseLibrary plugin;

    public PoseCommand(PoseLibrary plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String poseName = args.length >= 1 ? args[0] : null;
        String playerName = args.length >= 2 ? args[1] : null;

        NamespacedKey pose = poseName != null ? PoseCommand.LOOKUP_MAP.get(poseName) : null;

        if(sender.hasPermission("stevesseries.poselibrary.command")) {
            if(poseName != null) {
                if(pose != null) {
                    if(sender.hasPermission("stevesseries.poselibrary.command.pose." + poseName)) {
                        Player target;
                        if(playerName != null) {
                            if(!sender.hasPermission("stevesseries.poselibrary.command.others")) {
                                sender.sendMessage(this.plugin.getMessage("no-permission-others", pose, poseName));
                                return true;
                            }
                            target = Bukkit.getPlayerExact(playerName);
                            if(target == null) {
                                sender.sendMessage(this.plugin.getMessage("invalid-player", pose, playerName));
                                return true;
                            }
                            sender.sendMessage(this.plugin.getMessage("pose-set-other", pose, playerName, poseName));
                        } else {
                            if(sender instanceof Player) {
                                target = (Player) sender;
                            } else {
                                sender.sendMessage(this.plugin.getMessage("player-only", pose));
                                return true;
                            }
                        }
                        PoseFactory factory = PoseCommand.REGISTERED_POSES.get(pose);
                        if(factory != null) {
                            PoseManager.apply(target, factory.create(target, sender));
                            target.sendMessage(this.plugin.getMessage("pose-set", pose, poseName));
                        } else {
                            target.sendMessage(this.plugin.getMessage("pose-not-registered", pose, poseName));
                        }
                    } else {
                        sender.sendMessage(this.plugin.getMessage("no-permission-pose", pose, poseName));
                    }
                } else {
                    sender.sendMessage(this.plugin.getMessage("invalid-pose", pose, poseName));
                }
            } else {
                List<String> availablePoses = PoseCommand.getAvailablePoses(sender);
                sender.sendMessage(this.plugin.getMessage("available-poses", pose, availablePoses.size() > 0 ? String.join(this.plugin.getMessage("poses-separator", pose), availablePoses) : this.plugin.getMessage("no-poses", pose)));
            }
        } else {
            sender.sendMessage(this.plugin.getMessage("no-permission", pose));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 1) {
            List<String> poses = PoseCommand.getAvailablePoses(sender);
            if(args[0].length() > 0) {
                poses.removeIf(pose -> !pose.startsWith(args[0]));
            }
            return poses;
        } else if(args.length == 2) {
            List<String> players = new ArrayList<>();
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(!(sender instanceof Player) || ((Player) sender).canSee(player)) {
                    players.add(player.getName());
                }
            }
            if(args[1].length() > 0) {
                players.removeIf(player -> !player.startsWith(args[1]));
            }
            return players;
        } else {
            return Collections.emptyList();
        }
    }

    private static List<String> getAvailablePoses(Permissible permissible) {
        List<String> poses = new ArrayList<>();
        for(String name : PoseCommand.LOOKUP_MAP.keySet()) {
            if(permissible.hasPermission("stevesseries.poselibrary.command.pose." + name)) {
                poses.add(name);
            }
        }
        return poses;
    }

    /**
     * Register a pose for use with the /pose command
     * @param key unique key of the pose
     * @param factory pose factory
     */
    public static void registerPose(NamespacedKey key, PoseFactory factory) {
        PoseCommand.REGISTERED_POSES.put(key, factory);
    }
}