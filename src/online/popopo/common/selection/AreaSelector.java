package online.popopo.common.selection;


import net.minecraft.server.v1_12_R1.EnumParticle;
import online.popopo.common.message.Caster.PlayerCaster;
import online.popopo.common.message.Theme;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class AreaSelector {
    private static final String METADATA_KEY = "area_selection";
    private static final EnumParticle PARTICLE = EnumParticle.REDSTONE;

    private final JavaPlugin plugin;
    private final Theme theme;
    private final AreaViewer viewer;

    public AreaSelector(JavaPlugin p, Theme t) {
        this.plugin = p;
        this.theme = t;
        this.viewer = new AreaViewer(p, PARTICLE);

        Listener l = new SelectionListener();

        Bukkit.getPluginManager().registerEvents(l, p);
    }

    public void cancelSelectionIfHas(Player p) {
        this.viewer.hideArea(p);

        if (p.hasMetadata(METADATA_KEY)) {
            p.removeMetadata(METADATA_KEY, plugin);
        }
    }

    public void requestSelection(Player p) {
        Location[] v = new Location[2];
        MetadataValue m;

        cancelSelectionIfHas(p);
        m = new FixedMetadataValue(plugin, v);
        p.setMetadata(METADATA_KEY, m);
    }

    public Cuboid getSelection(Player p) {
        if (!p.hasMetadata(METADATA_KEY)) {
            return null;
        }

        MetadataValue m;

        m = p.getMetadata(METADATA_KEY).get(0);
        cancelSelectionIfHas(p);

        Location[] v = (Location[]) m.value();

        if (v[0] == null || v[1] == null) {
            return null;
        }

        return new Cuboid(v[0].getWorld(), v[0], v[1]);
    }

    private class SelectionListener implements Listener {
        @EventHandler(ignoreCancelled = true)
        public void onClickBreak(PlayerInteractEvent e) {
            Player p = e.getPlayer();

            if (!p.hasMetadata(METADATA_KEY)) {
                return;
            }

            PlayerCaster c = new PlayerCaster(theme, p);
            Location l = e.getClickedBlock().getLocation();
            Action a = e.getAction();
            MetadataValue m;

            m = p.getMetadata(METADATA_KEY).get(0);

            Location[] v = (Location[]) m.value();

            if (a == Action.LEFT_CLICK_BLOCK) {
                v[0] = l;
                c.castBar("selected start point");
            }

            if (a == Action.RIGHT_CLICK_BLOCK) {
                v[1] = l;
                c.castBar("selected end point");
            }

            if (v[0] != null && v[1] != null) {
                viewer.showArea(p, getSelection(p));
            }

            p.removeMetadata(METADATA_KEY, plugin);
            p.setMetadata(METADATA_KEY, m);
            e.setCancelled(true);
        }

        @EventHandler
        public void onTeleport(PlayerTeleportEvent e) {
            cancelSelectionIfHas(e.getPlayer());
        }

        @EventHandler
        public void onLogout(PlayerQuitEvent e) {
            cancelSelectionIfHas(e.getPlayer());
        }
    }
}
