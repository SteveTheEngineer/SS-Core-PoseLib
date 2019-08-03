package ru.ste.stevesseries.coreposelib;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class Pose implements Listener {
    public abstract void to(Player p);
    public abstract void from(Player p);
    public abstract void renderFor(Player p, Player posedP);
    public abstract void unrenderFor(Player p, Player posedP);
}