package ru.ste.stevesseries.coreposelib;

import org.bukkit.*;

public class PoseDurationTask implements Runnable {
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            PoseLibApi api = Bukkit.getServicesManager().getRegistration(PoseLibApi.class).getProvider();
            if(api.getPose(p).isPresent()) {
                if(api.getPoseDuration(p).get() > 0) {
                    api.setPoseDuration(p, api.getPoseDuration(p).get() - 1L);
                }
            }
        });
    }
}
