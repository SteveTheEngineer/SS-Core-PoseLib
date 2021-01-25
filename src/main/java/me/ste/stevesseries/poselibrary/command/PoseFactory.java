package me.ste.stevesseries.poselibrary.command;

import me.ste.stevesseries.poselibrary.pose.Pose;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface PoseFactory {
    Pose create(Player target, CommandSender sender);
}