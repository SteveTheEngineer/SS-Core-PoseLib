package ru.ste.stevesseries.coreposelib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

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
            if(!p.isFlying() && p.getAllowFlight()) p.setFlying(true);
            if(p.getLocation().distanceSquared(d.pos.clone().subtract(0, 1.3, 0)) > 0.4) p.teleport(d.pos.clone().subtract(0, 1.3, 0));

            String matName = p.getLocation().getBlock().getRelative(BlockFace.UP).getType().name();
            if(matName.endsWith("AIR") || matName.equals("WATER")) {
                if(PoseManager.getShiftsLeft(p) > 0) {
                    PoseManager.setShiftsLeft(p, PoseManager.getShiftsLeft(p) + 1);
                }
                plugin.getApi().resetPose(p);
                p.teleport(p.getLocation().getBlock().getRelative(d.bf.getOppositeFace()).getLocation().subtract(0, 1, 0));
                plugin.getApi().setPose(p, Laying.class);
            }
        });
        plugin.getApi().getPose(Sitting.class).get().ARMORSTANDS.forEach((pu, au) -> {
            Player p = Bukkit.getPlayer(pu);
            String matName = p.getLocation().getBlock().getType().name();
            if(matName.endsWith("AIR") || matName.equals("WATER")) {
                int fd = 4 * PoseManager.getShiftsLeft(p);
                PoseManager.unsetPose(p);
                p.setFallDistance(fd);
            }
        });
    }
}