package online.popopo.common.selection;


import net.minecraft.server.v1_12_R1.EnumParticle;
import online.popopo.common.message.Caster.PlayerCaster;
import online.popopo.common.message.Formatter;
import online.popopo.common.message.Theme;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class AreaSelector {
    private static final String METADATA_KEY = "area_selection";
    private static final EnumParticle PARTICLE = EnumParticle.REDSTONE;

    private final JavaPlugin plugin;
    private final Formatter formatter;
    private final AreaViewer viewer;

    public AreaSelector(JavaPlugin p, Formatter f) {
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
        if (!p.hasMetadata(METADATA_KEY)) {
            return null;
        }

        MetadataValue m;

        m = p.getMetadata(METADATA_KEY).get(0);
        disableSelectionMode(p);

        Location[] v = (Location[]) m.value();

        if (v[0] == null || v[1] == null) {
            return null;
        }

        return new Cuboid(v[0].getWorld(), v[0], v[1]);
    }

    private class SelectionListener implements Listener {
        @EventHandler(ignoreCancelled = true)
        public void onClickBlock(PlayerInteractEvent e) {
            Player p = e.getPlayer();

            if (!p.hasMetadata(METADATA_KEY)) {
                return;
            }

            PlayerCaster c = new PlayerCaster(formatter, p);
            Location l = e.getClickedBlock().getLocation();
            MetadataValue m;

            m = p.getMetadata(METADATA_KEY).get(0);

            Location[] v = (Location[]) m.value();

            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                v[0] = l;
                c.castBar("selected start point");
            }

            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                v[1] = l;
                c.castBar("selected end point");
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
