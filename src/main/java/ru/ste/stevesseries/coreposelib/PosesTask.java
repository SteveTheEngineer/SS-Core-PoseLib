package ru.ste.stevesseries.coreposelib;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;

public class PosesTask implements Runnable {

    private SSCorePoseLib plugin;

    public PosesTask(SSCorePoseLib plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getApi().getPose(Laying.class).get().POSED.forEach((u, d) -> {
            Player p = Bukkit.getPlayer(u);
            p.removePotionEffect(PotionEffectType.INVISIBILITY);
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20, 0, false, false, false));
            if(!p.isFlying() && p.getAllowFlight()) {
                p.setFlying(true);
            }
            if(p.getLocation().distanceSquared(d.pos.clone().subtract(0, 1.3, 0)) > 0.4) {
                p.teleport(d.pos.clone().subtract(0, 1.3, 0));
            }

            String matName = p.getLocation().getBlock().getRelative(BlockFace.UP).getType().name();
            if(matName.endsWith("AIR") || matName.equals("WATER")) {
                if(plugin.getApi().getShiftsLeft(p).isPresent() && plugin.getApi().getShiftsLeft(p).get() > 0) {
                    plugin.getApi().setShiftsLeft(p, plugin.getApi().getShiftsLeft(p).get() + 1);
                }
                int prevsl = plugin.getApi().getShiftsLeft(p).get();
                long prevd = plugin.getApi().getPoseDuration(p).get();
                plugin.getApi().resetPose(p);
                p.teleport(p.getLocation().getBlock().getRelative(d.bf.getOppositeFace()).getLocation().subtract(0, 1, 0));
                plugin.getApi().setPose(p, Laying.class, prevd, prevsl);
            }
        });
        plugin.getApi().getPose(Sitting.class).get().ARMORSTANDS.forEach((pu, au) -> {
            Player p = Bukkit.getPlayer(pu);
            String matName = p.getLocation().getBlock().getType().name();
            if(matName.endsWith("AIR") || matName.equals("WATER")) {
                int fd = 4 * plugin.getApi().getShiftsLeft(p).get();
                plugin.getApi().resetPose(p);
                p.setFallDistance(fd);
            }
        });
    }
}
