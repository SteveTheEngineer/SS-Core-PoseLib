package me.ste.stevesseries.poselibrary.pose;

import org.bukkit.entity.Player;

public abstract class Pose {
    protected final Player player;

    public Pose(Player player) {
        this.player = player;
    }

    /**
     * Called when this pose gets applied to the player
     */
    public abstract void init();

    /**
     * Called when the player pose has been set to another pose
     */
    public abstract void destroy();

    /**
     * Called when a player sees the player. <strong>Not implemented yet!</strong>
     * @param observer the observer
     */
    public abstract void doTrickery(Player observer);

    /**
     * Called when a player stops seeing the player. <strong>Not implemented yet!</strong>
     * @param observer the observer
     */
    public abstract void undoTrickery(Player observer);
}