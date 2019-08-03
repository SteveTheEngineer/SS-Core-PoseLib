package ru.ste.stevesseries.coreposelib;

import org.bukkit.Bukkit;

public class UpdaterTask implements Runnable {
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            Bukkit.getOnlinePlayers().forEach(p2 -> {
                Pose po = SSCorePoseLib.getPose(p2);
                if(po != null) {
                    po.unrenderFor(p, p2);
                    po.renderFor(p, p2);
                }
            });
        });
    }
}