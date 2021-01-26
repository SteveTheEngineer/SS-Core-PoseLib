package me.ste.stevesseries.poselibrary;

import me.ste.stevesseries.poselibrary.event.PlayerPoseChangeEvent;
import me.ste.stevesseries.poselibrary.pose.Pose;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PoseManager {
    public static final Map<UUID, Pose> POSES = new HashMap<>();

    private PoseManager() {}

    /**
     * Apply a pose to the player
     * @param player target player
     * @param pose pose. null for standing
     * @param force if true, the event won't be called before setting the pose
     */
    public static void apply(Player player, Pose pose, boolean force) {
        PlayerPoseChangeEvent event = new PlayerPoseChangeEvent(player, PoseManager.POSES.get(player.getUniqueId()), pose);
        if(!force) {
            Bukkit.getPluginManager().callEvent(event);
        }
        Pose pose2 = event.getTo();
        if(PoseManager.POSES.containsKey(player.getUniqueId())) {
            PoseManager.POSES.get(player.getUniqueId()).onDestruction();
        }
        if(pose != null) {
            PoseManager.POSES.put(player.getUniqueId(), pose2);
            pose2.onCreation();
        } else {
            PoseManager.POSES.remove(player.getUniqueId());
        }
    }

    /**
     * Apply a pose to the player. Same as {@link PoseManager#apply(Player, Pose, boolean)}, but force is always false
     * @param player target player
     * @param pose pose. null for standing
     */
    public static void apply(Player player, Pose pose) {
        PoseManager.apply(player, pose, false);
    }

    /**
     * Get the pose of the player
     * @param player target player
     * @return pose. null for standing
     */
    public static Pose getPose(Player player) {
        return PoseManager.POSES.get(player.getUniqueId());
    }
}