package ru.ste.stevesseries.coreposelib;

import org.bukkit.entity.Player;

/**
 * Represents a pose of the player character.
 */
public interface Pose {

    /**
     * Transforms the specified {@link Player} to the pose.
     *
     * @param p the player which you want to transform to that pose
     */
    void transform(Player p);

    /**
     * Normalizes the specified {@link Player} to standing position.
     *
     * @param p the player which you need to normalize
     * @throws IllegalArgumentException if the player is not transformed to that pose
     */
    void normalize(Player p);

    /**
     * Shows the specified posed {@link Player} to the specified player
     *
     * @param p the player to show to
     * @param posedP the positioned player
     * @throws IllegalArgumentException if the positioned player is not in that position
     */
    void showFor(Player p, Player posedP);

    /**
     * Hides the specified posed {@link Player} to the specified player.
     *
     * @param p the player to hide from
     * @param posedP the positioned player
     */
    void hideFor(Player p, Player posedP);
}