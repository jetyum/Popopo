package online.popopo.common.selection;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class AreaViewer {
    private static final String METADATA_KEY = "cuboid_effect";

    private final Plugin plugin;
    private final Particle particle;

    public AreaViewer(Plugin plugin, Particle p) {
        this.plugin = plugin;
        this.particle = p;
    }

    private Set<Location> getLocations(Cuboid c) {
        Set<Location> locations = new HashSet<>();
        World w = c.getWorld();
        Vector[] vertex = c.getVertex();
        int[][] sideIndex = {
                {0, 2}, {0, 4}, {6, 2}, {6, 4},
                {0, 1}, {2, 3}, {4, 5}, {6, 7},
                {1, 3}, {1, 5}, {7, 3}, {7, 5}
        };

        for (int[] index : sideIndex) {
            Vector a = vertex[index[0]].clone();
            Vector b = vertex[index[1]].clone();
            Vector dir = b.clone().subtract(a);
            double div = b.distance(a) * 2.0;
            double mul = 1.0 / div;
            Vector diff = dir.multiply(mul);
            int num = (int) div;

            for (int i = 0; i <= num; i++, a.add(diff)) {
                locations.add(a.toLocation(w));
            }
        }

        return locations;
    }

    public void hideArea(Player p) {
        if (p.hasMetadata(METADATA_KEY)) {
            MetadataValue m;

            m = p.getMetadata(METADATA_KEY).get(0);
            ((BukkitTask) m.value()).cancel();
            p.removeMetadata(METADATA_KEY, plugin);
        }
    }

    public void showArea(Player p, Cuboid c) {
        World w = c.getWorld();
        Set<Location> locations = getLocations(c);
        MetadataValue m;

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!p.getWorld().equals(w)) {
                    return;
                }

                for (Location l : locations) {
                    p.spawnParticle(particle, l, 0);
                }
            }
        }.runTaskTimer(plugin, 0, 10);

        hideArea(p);
        m = new FixedMetadataValue(plugin, task);
        p.setMetadata(METADATA_KEY, m);
    }
}
