package me.ste.stevesseries.poselibrary.event;

import me.ste.stevesseries.poselibrary.pose.Pose;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerPoseChangeEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final Pose from;
    private Pose to;

    public PlayerPoseChangeEvent(Player player, Pose from, Pose to) {
        this.player = player;
        this.from = from;
        this.to = to;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Pose getFrom() {
        return this.from;
    }

    public Pose getTo() {
        return this.to;
    }

    public void setTo(Pose to) {
        this.to = to;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerPoseChangeEvent.HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return PlayerPoseChangeEvent.HANDLER_LIST;
    }
}