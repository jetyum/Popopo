package online.popopo.popopo.portal;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class PortalListener implements Listener {
    private final Map<String, Portal> portals;

    public PortalListener(Map<String, Portal> m) {
        this.portals = m;
    }

    private Portal getPortalFrom(Location l) {
        for (Portal p : portals.values()) {
            if (p.getArea().contains(l)) {
                return p;
            }
        }

        return null;
    }

    @EventHandler
    public void onClickBlock(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Portal from = getPortalFrom(p.getLocation());

        if (from == null || !from.hasDestination()) {
            return;
        }

        String toName = from.getDestination();
        Portal to = portals.get(toName);

        if (to == null) {
            return;
        }

        Location l = to.getAppearanceLocation();

        l.setYaw(p.getLocation().getYaw());
        l.setPitch(p.getLocation().getPitch());
        p.teleport(l);
        e.setCancelled(true);
    }
}
