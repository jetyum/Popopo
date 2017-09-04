package online.popopo.popopo.domain;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.*;

public class Domain {
    private final String name;

    public Domain(World w) {
        String name = w.getName();

        this.name = name.split("_")[0];
    }

    public Domain(Location l) {
        this(l.getWorld());
    }

    public static Domain getMain() {
        World w = Bukkit.getWorlds().get(0);

        return new Domain(w);
    }

    public String getName() {
        return name;
    }

    public World getMainWorld() {
        return Bukkit.getWorld(name);
    }

    public PlayerData getPlayerData(Player p) {
        World w = getMainWorld();
        File dir = w.getWorldFolder();
        StringBuilder s = new StringBuilder();

        s.append(dir.getPath());
        s.append("/playerdata/");
        s.append(p.getUniqueId());
        s.append(".dat");

        File dat = new File(s.toString());

        return new PlayerData(dat);
    }

    public Location getSpawnLocation() {
        World w = getMainWorld();

        return w.getSpawnLocation();
    }

    public boolean available() {
        World w = getMainWorld();

        if (w == null) {
            return false;
        }

        return w.getWorldFolder().exists();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Domain) {
            Domain d = (Domain) o;

            return name.equals(d.name);
        } else {
            return false;
        }
    }
}