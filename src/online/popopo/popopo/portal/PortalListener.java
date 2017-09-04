package online.popopo.popopo.portal;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PortalListener implements Listener {
    private final PortalList portals;

    public PortalListener(JavaPlugin p, PortalList l) {
        this.portals = l;

        Bukkit.getPluginManager().registerEvents(this, p);
    }

    @EventHandler
    public void onClickBlock(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Portal from = portals.getPortal(p.getLocation());

        if (from == null || !from.hasDestination()) {
            return;
        }

        String toName = from.getDestination();
        Portal to = portals.getPortal(toName);

        if (to == null) {
            return;
        }

        Location l = to.getAppearanceLocation();

        l.setYaw(p.getLocation().getYaw());
        l.setPitch(p.getLocation().getPitch());
        p.teleport(l);
        p.playSound(l, Sound.ENTITY_EGG_THROW,
                SoundCategory.PLAYERS, 1.0f, 1.0f);
        e.setCancelled(true);
    }
}
