package ru.ste.stevesseries.coreposelib;

import org.bukkit.*;

import java.util.*;

public class UpdaterTask implements Runnable {

    private SSCorePoseLib plugin;

    public UpdaterTask(SSCorePoseLib plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> Bukkit.getOnlinePlayers().forEach(p2 -> {
            Optional< Pose > poseOptional = plugin.getApi().getPose(p2);
            if(poseOptional.isPresent()) {
                Pose pose = poseOptional.get();
                pose.hideFor(p, p2);
                pose.showFor(p, p2);
            }
        }));
    }
}
