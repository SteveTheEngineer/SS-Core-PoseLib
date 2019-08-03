package ru.ste.stevesseries.coreposelib;

import java.util.Optional;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PoseManager {
    private static Map<UUID, Long> duration = new HashMap<>();
    private static Map<UUID, Integer> shiftsLeft = new HashMap<>();
    private static SSCorePoseLib plugin = SSCorePoseLib.getPlugin(SSCorePoseLib.class);

    public static void unsetPose(Player p) {
        plugin.getApi().resetPose(p);
        duration.remove(p.getUniqueId());
        shiftsLeft.remove(p.getUniqueId());
    }
    public static boolean setPose(Player p, Pose po, long duration, int shifts) {
        if(!getPose(p).isPresent()) {
            return false;
        }
        if(po == null) {
            return false;
        }

        if(duration > 0) {
            PoseManager.duration.put(p.getUniqueId(), duration);
        } else {
            PoseManager.duration.put(p.getUniqueId(), 0L);
        }
        if(shifts > 0) {
            PoseManager.shiftsLeft.put(p.getUniqueId(), shifts);
        } else {
            PoseManager.shiftsLeft.put(p.getUniqueId(), 0);
        }

        plugin.getApi().setPose(p, po.getClass());
        return true;
    }

    public static long getPoseDuration(Player p) {
        return duration.getOrDefault(p.getUniqueId(), -1L);
    }

    public static boolean setPoseDuration(Player p, long d) {
        if(!getPose(p).isPresent()) {
            return false;
        }
        duration.put(p.getUniqueId(), d);
        return true;
    }

    public static int getShiftsLeft(Player p) {
        return shiftsLeft.getOrDefault(p.getUniqueId(), -1);
    }

    public static boolean setShiftsLeft(Player p, int shiftsLeft) {
        if(!getPose(p).isPresent()) {
            return false;
        }
        PoseManager.shiftsLeft.put(p.getUniqueId(), shiftsLeft);
        return true;
    }
    public static Optional<Pose> getPose(Player p) {
        return plugin.getApi().getPose(p);
    }
}