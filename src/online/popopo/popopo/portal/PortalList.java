package online.popopo.popopo.portal;

import online.popopo.common.config.Configurable;
import online.popopo.common.config.Parameter;
import org.bukkit.Location;

import java.util.Collection;
import java.util.Map;

public class PortalList implements Configurable {
    @Parameter("")
    private Map<String, Portal> portals;

    public Collection<Portal> getPortals() {
        return portals.values();
    }

    public void addPortal(Portal p) {
        portals.put(p.getName(), p);
    }

    public void removePortal(String name) {
        portals.remove(name);
    }

    public boolean hasPortal(String name) {
        return portals.containsKey(name);
    }

    public Portal getPortal(String name) {
        return portals.get(name);
    }

    public Portal getPortal(Location l) {
        for (Portal p : getPortals()) {
            if (p.getArea().contains(l)) {
                return p;
            }
        }

        return null;
    }

    @Override
    public String getSectionName() {
        return "portals";
    }
}
