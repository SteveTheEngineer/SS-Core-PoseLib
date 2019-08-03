package ru.ste.stevesseries.coreposelib;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerShiftStandupProgressEvent extends Event implements Cancellable {
    private boolean cancelled;
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Pose pose;
    private int shiftsLeft;

    public PlayerShiftStandupProgressEvent(Player player, Pose pose, int shiftsLeft) {
        this.player = player;
        this.pose = pose;
        this.shiftsLeft = shiftsLeft;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }
    public Pose getPose() {
        return pose;
    }
    public int getShiftsLeft() {
        return shiftsLeft;
    }
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}