package ru.ste.stevesseries.coreposelib;

import org.bukkit.Bukkit;

public class PoseManagerTask implements Runnable {
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if(PoseManager.getPose(p) != null) {
                if(PoseManager.getPoseDuration(p) > 0) {
                    PoseManager.setPoseDuration(p, PoseManager.getPoseDuration(p) - 1L);
                }
            }
        });
    }
}