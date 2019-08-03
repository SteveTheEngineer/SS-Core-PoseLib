package ru.ste.stevesseries.coreposelib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SSCorePoseLib extends JavaPlugin {
    public static SSCorePoseLib I;
    public static Map<UUID, Pose> poses;

    public static final Laying LAYING = new Laying();
    public static final Sitting SITTING = new Sitting();

    public SSCorePoseLib() {
        I = this;
        poses = new HashMap<>();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new UpdaterTask(), 0L, 300L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SSCorePoseLib.I, new PosesTask(), 0L, 1L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SSCorePoseLib.I, new PoseManagerTask(), 0L, 1L);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        Bukkit.getOnlinePlayers().forEach(op -> {
            setPose(op, null);
        });
    }

    public static Pose getPose(Player p) {
        return poses.getOrDefault(p.getUniqueId(), null);
    }
    public static void setPose(Player p, Pose pose) {
        if(poses.containsKey(p.getUniqueId())) {
            if(poses.get(p.getUniqueId()).equals(pose)) return;
            Bukkit.getOnlinePlayers().forEach(cp -> {
                poses.get(p.getUniqueId()).unrenderFor(cp, p);
            });
            poses.get(p.getUniqueId()).from(p);
            if(pose != null) {
                pose.to(p);
                poses.put(p.getUniqueId(), pose);

                Bukkit.getOnlinePlayers().forEach(cp -> {
                    pose.renderFor(cp, p);
                });
            }
            else {
                poses.remove(p.getUniqueId());
            }
        }
        else {
            if(pose != null) {
                pose.to(p);
                poses.put(p.getUniqueId(), pose);

                Bukkit.getOnlinePlayers().forEach(cp -> {
                    pose.renderFor(cp, p);
                });
            }
            else {
                poses.remove(p.getUniqueId());
            }
        }
    }
}