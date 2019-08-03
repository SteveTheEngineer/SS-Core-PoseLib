package ru.ste.stevesseries.coreposelib;

import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class EventListener implements Listener {

    private SSCorePoseLib plugin;

    public EventListener(SSCorePoseLib plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        plugin.getApi().getPosedPlayers().forEach((u, p) -> {
            Player pp = plugin.getServer().getPlayer(u);
            if(pp != null) {
                p.showFor(e.getPlayer(), pp);
            }
        });
    }
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getUpdaterTask().run());
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        plugin.getApi().resetPose(e.getEntity());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        plugin.getApi().resetPose(e.getPlayer());
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e) {
        UUID rightClickedPassenger = e.getRightClicked().getPassengers().get(0).getUniqueId();
        Map<UUID, Pose> rawPoses = plugin.getApi().getPosedPlayers();
        if(e.getRightClicked().getPassengers().size() > 0 && rawPoses.containsKey(rightClickedPassenger) && rawPoses.get(rightClickedPassenger) instanceof Sitting) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExit(EntityDismountEvent e) {
        if(plugin.getApi().getPosedPlayers().containsKey(e.getEntity().getUniqueId()) && plugin.getApi().getPosedPlayers().get(e.getEntity().getUniqueId()) instanceof Sitting) {
            e.setCancelled(true);
        }
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(PoseManager.getPose(p).isPresent()) {
                if(PoseManager.getShiftsLeft(p) > 0 && PoseManager.getPoseDuration(p) <= 0) {
                    PlayerShiftStandupProgressEvent ec = new PlayerShiftStandupProgressEvent(p, PoseManager.getPose(p).get(), PoseManager.getShiftsLeft(p));
                    Bukkit.getPluginManager().callEvent(ec);
                    if(!ec.isCancelled()) {
                        if(PoseManager.getShiftsLeft(p) == 1) {
                            PlayerShiftStandupProgressEvent ec2 = new PlayerShiftStandupProgressEvent(p, PoseManager.getPose(p).get(), 0);
                            Bukkit.getPluginManager().callEvent(ec);
                            if(!ec2.isCancelled()) {
                                PoseManager.unsetPose(p);
                            }
                        } else if(PoseManager.getShiftsLeft(p) > 1) {
                            PoseManager.setShiftsLeft(p, PoseManager.getShiftsLeft(p) - 1);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(plugin.getApi().getPosedPlayers().containsKey(e.getPlayer().getUniqueId()) && plugin.getApi().getPosedPlayers().get(e.getPlayer().getUniqueId()) instanceof Laying) {
            if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getTo().getZ()) {
                e.setCancelled(true);
            }
            else {
                if(e.getFrom().getYaw() != e.getTo().getYaw()) {
                    Bukkit.getOnlinePlayers().forEach(op -> plugin.getApi().getPose(Laying.class).get().setRotation(e.getPlayer(), op, e.getTo().getYaw()));
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
        check(e.getPlayer(), e);
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent e) {
        check(e.getPlayer(), e);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            check(p, e);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(plugin.getApi().getPosedPlayers().containsKey(p.getUniqueId()) && plugin.getApi().getPosedPlayers().get(p.getUniqueId()) instanceof Laying) {
                Bukkit.getOnlinePlayers().forEach(op -> plugin.getApi().getPose(Laying.class).get().animationDamage(p, op));
            }
        }
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent e) {
        check(e.getPlayer(), e);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        check(e.getPlayer(), e);
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            check((Player) e.getEntity(), e);
        }
    }

    @EventHandler
    public void onPickupArrow(PlayerPickupArrowEvent e) {
        check(e.getPlayer(), e);
    }

    @EventHandler
    public void onSwapHands(PlayerSwapHandItemsEvent e) {
        check(e.getPlayer(), e);
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent e) {
        check(e.getPlayer(), e);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        check((Player) e.getWhoClicked(), e);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        check(e.getPlayer(), e);
    }

    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent e) {
        if(e.isSneaking()) {
            if(PoseManager.getPose(e.getPlayer()).isPresent()) {
                if(PoseManager.getShiftsLeft(e.getPlayer()) > 0 && PoseManager.getPoseDuration(e.getPlayer()) <= 0) {
                    PlayerShiftStandupProgressEvent ec = new PlayerShiftStandupProgressEvent(e.getPlayer(), PoseManager.getPose(e.getPlayer()).get(), PoseManager.getShiftsLeft(e.getPlayer()));
                    Bukkit.getPluginManager().callEvent(ec);
                    if(!ec.isCancelled()) {
                        if(PoseManager.getShiftsLeft(e.getPlayer()) == 1) {
                            PlayerShiftStandupProgressEvent ec2 = new PlayerShiftStandupProgressEvent(e.getPlayer(), PoseManager.getPose(e.getPlayer()).get(), 0);
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

    private void check(Player player, Cancellable event) {
        if(plugin.getApi().getPosedPlayers().containsKey(player.getUniqueId()) && plugin.getApi().getPosedPlayers().get(player.getUniqueId()) instanceof Laying) {
            event.setCancelled(true);
        }
    }
}
