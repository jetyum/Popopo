package online.popopo.common.selection;

import online.popopo.common.message.Notice;
import online.popopo.common.message.UserNotice.PlayerNotice;
import online.popopo.common.message.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class AreaSelector {
    private static final String METADATA_KEY = "area_selection";
    private static final Particle PARTICLE = Particle.REDSTONE;

    private final Plugin plugin;
    private final Formatter formatter;
    private final AreaViewer viewer;

    public AreaSelector(Plugin p, Formatter f) {
        this.plugin = p;
        this.formatter = f;
        this.viewer = new AreaViewer(p, PARTICLE);

        Listener l = new SelectionListener();

        Bukkit.getPluginManager().registerEvents(l, p);
    }

    public void disableSelectionMode(Player p) {
        if (p.hasMetadata(METADATA_KEY)) {
            viewer.hideArea(p);
            p.removeMetadata(METADATA_KEY, plugin);
        }
    }

    public void enableSelectionMode(Player p) {
        Location[] v = new Location[2];
        MetadataValue m;

        disableSelectionMode(p);
        m = new FixedMetadataValue(plugin, v);
        p.setMetadata(METADATA_KEY, m);
    }

    public Cuboid getSelectedArea(Player p) {
        if (p.hasMetadata(METADATA_KEY)) {
            List<MetadataValue> list;

            list = p.getMetadata(METADATA_KEY);
            disableSelectionMode(p);

            if (!list.isEmpty()) {
                MetadataValue m = list.get(0);
                Location[] v = (Location[]) m.value();

                if (v[0] != null && v[1] != null) {
                    World w = v[0].getWorld();

                    return new Cuboid(w, v[0], v[1]);
                }
            }
        }

        return null;
    }

    private class SelectionListener implements Listener {
        @EventHandler(ignoreCancelled = true)
        public void onClickBlock(PlayerInteractEvent e) {
            Player p = e.getPlayer();

            if (!p.hasMetadata(METADATA_KEY)) {
                return;
            }

            PlayerNotice n = Notice.create(formatter, p);
            Location l = e.getClickedBlock().getLocation();
            MetadataValue m;

            m = p.getMetadata(METADATA_KEY).get(0);

            Location[] v = (Location[]) m.value();

            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                v[0] = l;
                n.toast("selected start point");
            }

            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                v[1] = l;
                n.toast("selected end point");
            }

            if (v[0] != null && v[1] != null) {
                viewer.showArea(p, getSelectedArea(p));
            }

            p.removeMetadata(METADATA_KEY, plugin);
            p.setMetadata(METADATA_KEY, m);
            e.setCancelled(true);
        }

        @EventHandler
        public void onTeleported(PlayerChangedWorldEvent e) {
            disableSelectionMode(e.getPlayer());
        }

        @EventHandler
        public void onLogout(PlayerQuitEvent e) {
            disableSelectionMode(e.getPlayer());
        }
    }
}
