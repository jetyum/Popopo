package online.popopo.popopo.protection;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Map;

public class Judge {
    private final Map<String, Reserve> reserves;
    private final Map<String, License> licenses;

    public Judge(Map<String, Reserve> r, Map<String, License> l) {
        this.reserves = r;
        this.licenses = l;
    }

    public Map<String, Reserve> getReserves() {
        return reserves;
    }

    public Map<String, License> getLicenses() {
        return licenses;
    }

    private Reserve getReserve(Location l) {
        for (Reserve r : reserves.values()) {
            if (r.getArea().contains(l)) {
                return r;
            }
        }

        return null;
    }

    private boolean can(Reserve r, String act) {
        License l = licenses.get(r.getLicense());

        return l != null && l.contains(act);

    }

    public boolean can(Player p, Location l, String act) {
        Reserve r = getReserve(l);
        String name = p.getName();

        return r == null || !r.hasMember(name) && can(r, act);

    }

    public boolean can(Player p, Block b, String act) {
        return can(p, b.getLocation(), act);
    }

    public boolean can(Player p, Entity e, String act) {
        return can(p, e.getLocation(), act);
    }

    public boolean can(Location l, String act) {
        Reserve r = getReserve(l);

        return r == null || can(r, act);
    }

    public boolean can(Block b, String act) {
        return can(b.getLocation(), act);
    }

    public boolean can(Entity e, String act) {
        return can(e.getLocation(), act);
    }
}
