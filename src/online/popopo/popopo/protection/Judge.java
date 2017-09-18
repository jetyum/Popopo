package online.popopo.popopo.protection;

import online.popopo.popopo.protection.Reserve.Priority;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Map;

public class Judge {
    private final Map<String, Reserve> reserves;
    private final Map<String, License> licenses;

    public Judge(Map<String, Reserve> r,
                 Map<String, License> l) {
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
        Priority priority = Priority.LOWEST;
        Reserve reserve = null;

        for (Reserve r : reserves.values()) {
            if (r.getArea().contains(l)) {
                Priority p = r.getPriority();

                if (p.compareTo(priority) <= 0) {
                    priority = p;
                    reserve = r;
                }
            }
        }

        return reserve;
    }

    private boolean allows(Reserve r, String a, String o) {
        License l = licenses.get(r.getLicense());

        return l != null && l.allows(a, o);
    }

    public boolean allows(Player p, Location l, String a) {
        Reserve r = getReserve(l);
        String n = p.getName();

        if (r == null) return true;
        if (r.getMembers().contains(n)) return true;

        return allows(r, a, License.OPTION_PLAYER);
    }

    public boolean allows(Player p, Block b, String a) {
        return allows(p, b.getLocation(), a);
    }

    public boolean allows(Player p, Entity e, String a) {
        return allows(p, e.getLocation(), a);
    }

    public boolean allows(Location l, String a, String o) {
        Reserve r = getReserve(l);

        if (r == null) return true;

        return allows(r, a, o);
    }

    public boolean allows(Block b, String a, String o) {
        return allows(b.getLocation(), a, o);
    }

    public boolean allows(Entity e, String a, String o) {
        return allows(e.getLocation(), a, o);
    }
}
