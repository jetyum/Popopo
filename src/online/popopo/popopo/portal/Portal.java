package online.popopo.popopo.portal;

import online.popopo.api.message.Guideable;
import online.popopo.api.selection.Cuboid;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Portal implements Serializable, Guideable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final Cuboid area;

    private String destination = null;

    public Portal(String name, Cuboid c) {
        this.name = name;
        this.area = c;
    }

    public String getName() {
        return name;
    }

    public Cuboid getArea() {
        return area;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String d) {
        destination = d;
    }

    public boolean hasDestination() {
        return destination != null;
    }

    public void clearDestination() {
        destination = null;
    }

    public Location getAppearanceLocation() {
        World w = area.getWorld();
        Vector max = area.getMaximum();
        Vector min = area.getMinimum();
        double yMin = min.getY();

        max.add(new Vector(1, 1, 1));

        Vector mid = max.midpoint(min);
        Vector bottom = mid.setY(yMin);
        Location l = bottom.toLocation(w);
        Block block = l.getBlock();

        while(true) {
            Block b = block.getRelative(0, 1, 0);

            if (block.isEmpty() && b.isEmpty()) {
                break;
            } else {
                block = b;
            }
        }

        l.setY(block.getY());

        return l;
    }

    @Override
    public String getLoreTitle() {
        return "Information of Portal";
    }

    @Override
    public List<String> getLore() {
        List<String> list = new ArrayList<>();

        list.add("world : " + area.getWorld().getName());
        list.add("size : " + area.getVolume());
        list.add("destination : " + destination);

        return list;
    }
}
