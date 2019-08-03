package ru.ste.stevesseries.coreposelib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class EventListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        SSCorePoseLib.poses.forEach((u, p) -> {
            Player pp = Bukkit.getPlayer(u);
            if(pp != null) {
                p.renderFor(e.getPlayer(), pp);
            }
        });
    }
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(SSCorePoseLib.I, () -> {
            Bukkit.getOnlinePlayers().forEach(p -> {
                Bukkit.getOnlinePlayers().forEach(p2 -> {
                    Pose po = SSCorePoseLib.getPose(p2);
                    if(po != null) {
                        po.unrenderFor(p, p2);
                        po.renderFor(p, p2);
                    }
                });
            });
        });
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        SSCorePoseLib.setPose(e.getEntity(), null);
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        SSCorePoseLib.setPose(e.getPlayer(), null);
    }
    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e) {
        if(e.getRightClicked().getPassengers().size() > 0 && SSCorePoseLib.poses.containsKey(e.getRightClicked().getPassengers().get(0).getUniqueId()) && SSCorePoseLib.poses.get(e.getRightClicked().getPassengers().get(0).getUniqueId()) instanceof Sitting) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onEntityExit(EntityDismountEvent e) {
        if(SSCorePoseLib.poses.containsKey(e.getEntity().getUniqueId()) && SSCorePoseLib.poses.get(e.getEntity().getUniqueId()) instanceof Sitting) {
            e.setCancelled(true);
        }
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(PoseManager.getPose(p) != null) {
                if(PoseManager.getShiftsLeft(p) > 0 && PoseManager.getPoseDuration(p) <= 0) {
                    PlayerShiftStandupProgressEvent ec = new PlayerShiftStandupProgressEvent(p, PoseManager.getPose(p), PoseManager.getShiftsLeft(p));
                    Bukkit.getPluginManager().callEvent(ec);
                    if(!ec.isCancelled()) {
                        if(PoseManager.getShiftsLeft(p) == 1) {
                            PlayerShiftStandupProgressEvent ec2 = new PlayerShiftStandupProgressEvent(p, PoseManager.getPose(p), 0);
                            Bukkit.getPluginManager().callEvent(ec);
                            if(!ec2.isCancelled()) PoseManager.unsetPose(p);
                        }
                        else if(PoseManager.getShiftsLeft(p) > 1) {
                            PoseManager.setShiftsLeft(p, PoseManager.getShiftsLeft(p) - 1);
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(SSCorePoseLib.poses.containsKey(e.getPlayer().getUniqueId()) && SSCorePoseLib.poses.get(e.getPlayer().getUniqueId()) instanceof Laying) {
            if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getTo().getZ()) {
                e.setCancelled(true);
            }
            else {
                if(e.getFrom().getYaw() != e.getTo().getYaw()) {
                    Bukkit.getOnlinePlayers().forEach(op -> {
                        SSCorePoseLib.LAYING.setRotation(e.getPlayer(), op, e.getTo().getYaw());
                    });
                }
                if(e.getFrom().getPitch() != e.getTo().getPitch()) {
                    if(e.getTo().getPitch() > 0) {
                        e.getTo().setPitch(0);
                        e.setTo(e.getTo());
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(SSCorePoseLib.poses.containsKey(e.getPlayer().getUniqueId()) && SSCorePoseLib.poses.get(e.getPlayer().getUniqueId()) instanceof Laying) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onBlockDamage(BlockDamageEvent e) {
        if(SSCorePoseLib.poses.containsKey(e.getPlayer().getUniqueId()) && SSCorePoseLib.poses.get(e.getPlayer().getUniqueId()) instanceof Laying) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if(SSCorePoseLib.poses.containsKey(p.getUniqueId()) && SSCorePoseLib.poses.get(p.getUniqueId()) instanceof Laying) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(SSCorePoseLib.poses.containsKey(p.getUniqueId()) && SSCorePoseLib.poses.get(p.getUniqueId()) instanceof Laying) {
                Bukkit.getOnlinePlayers().forEach(op -> {
                    SSCorePoseLib.LAYING.animationDamage(p, op);
                });
            }
        }
    }
    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent e) {
        if(SSCorePoseLib.poses.containsKey(e.getPlayer().getUniqueId()) && SSCorePoseLib.poses.get(e.getPlayer().getUniqueId()) instanceof Laying) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        if(SSCorePoseLib.poses.containsKey(e.getPlayer().getUniqueId()) && SSCorePoseLib.poses.get(e.getPlayer().getUniqueId()) instanceof Laying) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent e) {
        if(SSCorePoseLib.poses.containsKey(e.getPlayer().getUniqueId()) && SSCorePoseLib.poses.get(e.getPlayer().getUniqueId()) instanceof Laying) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPickupArrow(PlayerPickupArrowEvent e) {
        if(SSCorePoseLib.poses.containsKey(e.getPlayer().getUniqueId()) && SSCorePoseLib.poses.get(e.getPlayer().getUniqueId()) instanceof Laying) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onSwapHands(PlayerSwapHandItemsEvent e) {
        if(SSCorePoseLib.poses.containsKey(e.getPlayer().getUniqueId()) && SSCorePoseLib.poses.get(e.getPlayer().getUniqueId()) instanceof Laying) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent e) {
        if(SSCorePoseLib.poses.containsKey(e.getPlayer().getUniqueId()) && SSCorePoseLib.poses.get(e.getPlayer().getUniqueId()) instanceof Laying) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(SSCorePoseLib.poses.containsKey(e.getWhoClicked().getUniqueId()) && SSCorePoseLib.poses.get(e.getWhoClicked().getUniqueId()) instanceof Laying) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if(SSCorePoseLib.poses.containsKey(e.getPlayer().getUniqueId()) && SSCorePoseLib.poses.get(e.getPlayer().getUniqueId()) instanceof Laying) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent e) {
        if(e.isSneaking()) {
            if(PoseManager.getPose(e.getPlayer()) != null) {
                if(PoseManager.getShiftsLeft(e.getPlayer()) > 0 && PoseManager.getPoseDuration(e.getPlayer()) <= 0) {
                    PlayerShiftStandupProgressEvent ec = new PlayerShiftStandupProgressEvent(e.getPlayer(), PoseManager.getPose(e.getPlayer()), PoseManager.getShiftsLeft(e.getPlayer()));
                    Bukkit.getPluginManager().callEvent(ec);
                    if(!ec.isCancelled()) {
                        if(PoseManager.getShiftsLeft(e.getPlayer()) == 1) {
                            PlayerShiftStandupProgressEvent ec2 = new PlayerShiftStandupProgressEvent(e.getPlayer(), PoseManager.getPose(e.getPlayer()), 0);
                            Bukkit.getPluginManager().callEvent(ec);
                            if(!ec2.isCancelled()) PoseManager.unsetPose(e.getPlayer());
                        }
                        else if(PoseManager.getShiftsLeft(e.getPlayer()) > 1) {
                            PoseManager.setShiftsLeft(e.getPlayer(), PoseManager.getShiftsLeft(e.getPlayer()) - 1);
                        }
                    }
                }
            }
        }
    }
}
