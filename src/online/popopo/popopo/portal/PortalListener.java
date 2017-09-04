package online.popopo.popopo.portal;

import online.popopo.common.message.Caster.PlayerCaster;
import online.popopo.common.message.Theme;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PortalListener implements Listener {
    private final PortalList portals;
    private final Theme theme;

    public PortalListener(JavaPlugin plugin, PortalList p, Theme t) {
        this.portals = p;
        this.theme = t;

        Bukkit.getPluginManager().registerEvents(this, plugin);
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
        PlayerCaster c = new PlayerCaster(theme, p);

        l.setYaw(p.getLocation().getYaw());
        l.setPitch(p.getLocation().getPitch());
        p.teleport(l);
        c.castBar("teleported");
        e.setCancelled(true);
    }
}
