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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PoseLibApiImpl implements PoseLibApi {

  private Map<UUID, Pose> posesMap = new ConcurrentHashMap<>();
  private Map<Class<? extends Pose>, Pose> knownPoses = new ConcurrentHashMap<>();

  @Override
  public <T extends Pose> Optional<T> getPose(Class<T> poseClass) {
    if (!knownPoses.containsKey(poseClass)) {
      return Optional.empty();
    }
    return Optional.of((T) knownPoses.get(poseClass));
  }

  @Override
  public <T extends Pose> T registerPose(Class<T> poseClass, T pose) {
    knownPoses.put(poseClass, pose);
    return pose;
  }

  @Override
  public Optional<Pose> getPose(Player player) {
    return Optional.ofNullable(posesMap.get(player.getUniqueId()));
  }

  @Override
  public void resetPose(Player player) {
    Optional<Pose> poseOptional = getPose(player);
    if (poseOptional.isPresent()) {
      Pose pose = poseOptional.get();
      pose.normalize(player);
      for (Player all : Bukkit.getOnlinePlayers()) {
        pose.hideFor(all, player);
      }
      posesMap.remove(player.getUniqueId());
    }
  }

  @Override
  public void setPose(Player player, Class<? extends Pose> poseClass) {
    Optional<Pose> poseOptional = getPose(player);
    if (poseOptional.isPresent()) {
      posesMap.remove(player.getUniqueId());
      Pose currentPose = poseOptional.get();
      if (currentPose.getClass().isAssignableFrom(poseClass)) {
        return;
      }
    }
    Pose toSet = knownPoses.get(poseClass);
    Preconditions.checkNotNull(toSet, "New position not registered");
    toSet.transform(player);
    for (Player all : Bukkit.getOnlinePlayers()) {
      toSet.showFor(all, player);
    }
    posesMap.put(player.getUniqueId(), toSet);
  }

  @Override
  public Map<UUID, Pose> getPosedPlayers() {
    return ImmutableMap.copyOf(posesMap);
  }
}
