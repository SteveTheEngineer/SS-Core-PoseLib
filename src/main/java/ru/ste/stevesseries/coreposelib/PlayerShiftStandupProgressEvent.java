package ru.ste.stevesseries.coreposelib;

import org.bukkit.entity.*;
import org.bukkit.event.*;

/** Represents a event, called when player shifts stand up progress */
public class PlayerShiftStandupProgressEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private Player player;
    private Pose pose;
    private int shiftsLeft;

    public PlayerShiftStandupProgressEvent(Player player, Pose pose, int shiftsLeft) {
        this.player = player;
        this.pose = pose;
        this.shiftsLeft = shiftsLeft;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the player, which shifts stand up progress
     *
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the pose of which the player shifts
     *
     * @return pose
     */
    public Pose getPose() {
        return pose;
    }

    /**
     * Gets the shifts left
     *
     * @return shifts
     */
    public int getShiftsLeft() {
        return shiftsLeft;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    //

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    //
}
