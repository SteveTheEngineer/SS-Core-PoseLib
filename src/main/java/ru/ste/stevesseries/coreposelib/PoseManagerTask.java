package ru.ste.stevesseries.coreposelib;

import org.bukkit.Bukkit;

public class PoseManagerTask implements Runnable {

    private SSCorePoseLib plugin;

    public PoseManagerTask(SSCorePoseLib plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if(plugin.getApi().getPose(p).isPresent()) {
                if(plugin.getApi().getPoseDuration(p).get() > 0) {
                    plugin.getApi().setPoseDuration(p, plugin.getApi().getPoseDuration(p).get() - 1L);
                }
            }
        });
    }
}