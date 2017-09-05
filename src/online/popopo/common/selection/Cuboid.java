package online.popopo.common.selection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.io.Serializable;

public class Cuboid implements Serializable {
    private final String world;
    private final int x1, y1, z1;
    private final int x2, y2, z2;

    public Cuboid(World w, Vector v1, Vector v2) {
        this.world = w.getName();
        this.x1 = v1.getBlockX();
        this.y1 = v1.getBlockY();
        this.z1 = v1.getBlockZ();
        this.x2 = v2.getBlockX();
        this.y2 = v2.getBlockY();
        this.z2 = v2.getBlockZ();
    }

    public Cuboid(World w, Location l1, Location l2) {
        this(w, l1.toVector(), l2.toVector());
    }

    public World getWorld() {
        return Bukkit.getWorld(world);
    }

    public Vector getMaximum() {
        Vector v1 = new Vector(x1, y1, z1);
        Vector v2 = new Vector(x2, y2, z2);

        return Vector.getMaximum(v1, v2);
    }

    public Vector getMinimum() {
        Vector v1 = new Vector(x1, y1, z1);
        Vector v2 = new Vector(x2, y2, z2);

        return Vector.getMinimum(v1, v2);
    }

    public Vector[] getVertex() {
        Vector max = getMaximum();
        Vector min = getMinimum();
        double xMin = min.getX();
        double xMax = max.getX() + 1.0;
        double yMin = min.getY();
        double yMax = max.getY() + 1.0;
        double zMin = min.getZ();
        double zMax = max.getZ() + 1.0;

        return new Vector[] {
                new Vector(xMin, yMin, zMin),
                new Vector(xMax, yMin, zMin),
                new Vector(xMin, yMax, zMin),
                new Vector(xMax, yMax, zMin),
                new Vector(xMin, yMin, zMax),
                new Vector(xMax, yMin, zMax),
                new Vector(xMin, yMax, zMax),
                new Vector(xMax, yMax, zMax)
        };
    }

    public int getVolume() {
        int x = Math.abs(x1 - x2);
        int y = Math.abs(y1 - y2);
        int z = Math.abs(z1 - z2);

        return x * y * z;
    }

    public boolean contains(Location l) {
        World w = l.getWorld();

        if (!w.getName().equals(world)) {
            return false;
        }

        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();

        if ((x - x1) * (x - x2) > 0) {
            return false;
        }

        if ((y - y1) * (y - y2) > 0) {
            return false;
        }

        if ((z - z1) * (z - z2) > 0) {
            return false;
        }

        return true;
    }
}
