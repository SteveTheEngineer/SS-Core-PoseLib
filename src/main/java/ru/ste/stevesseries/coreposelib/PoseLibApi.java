/*
 * Copyright 2019 Ivan Pekov (MrIvanPlays)
 * Copyright 2019 contributors

 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 **/
package ru.ste.stevesseries.coreposelib;

import org.bukkit.entity.*;

import java.util.*;

/** Represents api part of the pose lib */
public interface PoseLibApi {

    /**
     * Returns pose instance of the specified pose class.
     *
     * @param poseClass the pose class of which you want to get the pose
     * @param <T>       pose type
     *
     * @return optional of pose if registered, else empty optional
     */
    < T extends Pose > Optional< T > getPose(Class< T > poseClass);

    /**
     * Registers pose for use
     *
     * @param poseClass the registered pose's class
     * @param pose      pose instance
     * @param <T>       pose type
     *
     * @return pose instance
     */
    < T extends Pose > T registerPose(Class< T > poseClass, T pose);

    /**
     * Returns pose object of the player.
     *
     * @param player the player of which you want to get the pose
     *
     * @return optional of pose if present, empty optional if pose is normal
     */
    Optional< Pose > getPose(Player player);

    /**
     * Sets the player's pose back to normal.
     *
     * @param player the player of which you want to reset the pose
     */
    void resetPose(Player player);

    /**
     * Sets a new pose of the player.
     *
     * <p><i>The pose will get set if the poseClass matches a registered pose.</i>
     *
     * @param player    the player of which you want to set the pose
     * @param poseClass new pose's class
     * @param duration  how long player will need to wait until starting to press shift (in ticks), use
     *                  0 to remove duration
     * @param shifts    amount of times player need to press in order to stand up, use 0 to remove
     *                  ability to stand up
     *
     * @throws IllegalArgumentException if the pose is unknown
     */
    void setPose(Player player, Class< ? extends Pose > poseClass, long duration, int shifts);

    /**
     * Gets amount of times player need to press shift in order to stand up left
     *
     * @param player the player of which you want to get the data
     *
     * @return optional of integer if present, empty optional if none
     */
    Optional< Integer > getShiftsLeft(Player player);

    /**
     * Sets a new amount of times player need to press shift in order to stand up left
     *
     * @param player    the player of which you want to get the data
     * @param newShifts the new shifts
     */
    void setShiftsLeft(Player player, int newShifts);

    /**
     * Gets player pose duration
     *
     * @param player the player of which you want to get the pose duration
     *
     * @return optional of long if present, empty optional if none
     */
    Optional< Long > getPoseDuration(Player player);

    /**
     * Sets a new player pose duration
     *
     * @param player             player, whose duration you want to set
     * @param playerPoseDuration new duration
     */
    void setPoseDuration(Player player, long playerPoseDuration);

    /**
     * Returns a raw, unmodifiable copy of all posed players with their positions
     *
     * @return map copy
     */
    Map< UUID, Pose > getPosedPlayers();
}
