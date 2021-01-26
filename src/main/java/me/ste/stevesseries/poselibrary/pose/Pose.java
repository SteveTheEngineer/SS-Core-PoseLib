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
    public abstract void onCreation();

    /**
     * Called when the player pose has been set to another pose
     */
    public abstract void onDestruction();

    /**
     * Called when a player sees the player. Usually used for when working with packets/fake entities. <strong>Not implemented yet!</strong>
     * @param observer the observer
     */
    public abstract void onNewObserver(Player observer);

    /**
     * Called when a player stops seeing the player. Usually used for when working with packets/fake entities. <strong>Not implemented yet!</strong>
     * @param observer the observer
     */
    public abstract void onObserverLost(Player observer);
}