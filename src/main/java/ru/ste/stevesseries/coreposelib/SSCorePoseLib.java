package ru.ste.stevesseries.coreposelib;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class SSCorePoseLib extends JavaPlugin {

    private static PoseLibApi poseLibApi;
    private UpdaterTask updaterTask;

    @Override
    public void onEnable() {
        poseLibApi = new PoseLibApiImpl();
        poseLibApi.registerPose(Laying.class, new Laying());
        poseLibApi.registerPose(Sitting.class, new Sitting(this));
        getServer().getServicesManager().register(PoseLibApi.class, poseLibApi, this, ServicePriority.Highest);
        updaterTask = new UpdaterTask(this);
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, updaterTask, 0L, 300L);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new PosesTask(this), 0L, 1L);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new PoseManagerTask(), 0L, 1L);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

    public PoseLibApi getApi() {
        return poseLibApi;
    }

    public UpdaterTask getUpdaterTask() {
        return updaterTask;
    }
}