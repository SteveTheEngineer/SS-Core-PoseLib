package me.ste.stevesseries.poselibrary;

import me.ste.stevesseries.base.GenericUtil;
import me.ste.stevesseries.poselibrary.command.PoseCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

public class PoseLibrary extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PoseListener(), this);

        this.getCommand("pose").setExecutor(new PoseCommand(this));
        this.getCommand("poselibraryreload").setExecutor((sender, command, label, args) -> {
            if(sender.hasPermission("stevesseries.poselibrary.reload")) {
                this.reloadPluginConfiguration();
                sender.sendMessage(this.getMessage("configuration-reloaded", null));
            } else {
                sender.sendMessage(this.getMessage("reload-no-permission", null));
            }
            return true;
        });

        this.reloadPluginConfiguration();

        PoseCommand.registerPose(new NamespacedKey(this, "standing"), (target, sender) -> null);
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
        for(UUID key : PoseManager.POSES.keySet()) {
            Player player = Bukkit.getPlayer(key);
            if(player != null) {
                PoseManager.apply(player, null);
            }
        }
        PoseManager.POSES.clear();
    }

    private void reloadPluginConfiguration() {
        if(!Files.exists(this.getDataFolder().toPath().resolve("config.yml"))) {
            this.saveDefaultConfig();
        }

        this.reloadConfig();
        PoseCommand.LOOKUP_MAP.clear();
        ConfigurationSection poses = this.getConfig().getConfigurationSection("poses");
        for(String keyString : poses.getKeys(false)) {
            ConfigurationSection pose = poses.getConfigurationSection(keyString);
            NamespacedKey key = GenericUtil.parseNamespacedKey(keyString);
            PoseCommand.LOOKUP_MAP.put(pose.getString("name"), key);
        }
    }

    public String getMessage(String key, NamespacedKey poseKey, Object... arguments) {
        String configKey = null;
        if(poseKey != null) {
            configKey = "poses." + poseKey.toString() + ".messages." + key;
        }
        if(configKey == null || !this.getConfig().isString(configKey)) {
            configKey = "messages." + key;
        }
        if(this.getConfig().isString(configKey)) {
            return ChatColor.translateAlternateColorCodes('&', String.format(Objects.requireNonNull(this.getConfig().getString(configKey)), arguments));
        } else {
            return key;
        }
    }
}