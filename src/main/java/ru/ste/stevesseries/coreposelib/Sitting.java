package ru.ste.stevesseries.coreposelib;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Sitting extends Pose {
    public Map<UUID, UUID> ARMORSTANDS = new HashMap<>();

    @Override
    public void to(Player p) {
        ArmorStand s = (ArmorStand) p.getLocation().getWorld().spawnEntity(p.getLocation().subtract(0, 0.2, 0), EntityType.ARMOR_STAND);
        s.setVisible(false);
        s.setSilent(true);
        s.setBasePlate(false);
        s.setMarker(true);
        s.setInvulnerable(true);
        s.setAI(false);
        s.setGravity(false);
        s.addPassenger(p);
        ARMORSTANDS.put(p.getUniqueId(), s.getUniqueId());
    }

    @Override
    public void from(Player p) {
        ArmorStand s = (ArmorStand) Bukkit.getEntity(ARMORSTANDS.get(p.getUniqueId()));
        s.remove();
        Bukkit.getScheduler().scheduleSyncDelayedTask(SSCorePoseLib.I, () -> p.teleport(p.getLocation().clone().add(0, 0.6, 0)));
    }

    @Override
    public void renderFor(Player p, Player posedP) {

    }

    @Override
    public void unrenderFor(Player p, Player posedP) {

    }
}
