package online.popopo.common.selection;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.Overridden;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class AreaViewer {
    private static final String METADATA_KEY = "cuboid_effect";

    private final JavaPlugin plugin;
    private final EnumParticle particle;

    public AreaViewer(JavaPlugin plugin, EnumParticle p) {
        this.plugin = plugin;
        this.particle = p;
    }

    private Set<Vector> getVectors(Cuboid c) {
        Set<Vector> vectors = new HashSet<>();
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
                vectors.add(a.clone());
            }
        }

        return vectors;
    }

    private Packet createPacket(Vector v) {
        return new PacketPlayOutWorldParticles(
                particle, true,
                (float) v.getX(),
                (float) v.getY(),
                (float) v.getZ(),
                0.0f, 0.0f, 0.0f, 0.0f, 0);
    }

    private Set<Packet> createPackets(Set<Vector> set) {
        Set<Packet> packets = new HashSet<>();

        for (Vector v : set) {
            packets.add(createPacket(v));
        }

        return packets;
    }

    public void hideArea(Player p) {
        if (p.hasMetadata(METADATA_KEY)) {
            MetadataValue m;

            m = p.getMetadata(METADATA_KEY).get(0);
            ((BukkitTask) m.value()).cancel();
            p.removeMetadata(METADATA_KEY, plugin);
        }
    }

    public void showArea(Player target, Cuboid c) {
        World w = c.getWorld();
        CraftPlayer p = (CraftPlayer) target;
        EntityPlayer e = p.getHandle();
        PlayerConnection i = e.playerConnection;
        Set<Vector> vectors = getVectors(c);
        Set<Packet> packets = createPackets(vectors);
        MetadataValue m;

        BukkitTask task = new BukkitRunnable() {
            @Overridden
            public void run() {
                if (target.getWorld().equals(w)) {
                    for (Packet packet : packets) {
                        i.sendPacket(packet);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 10);

        hideArea(p);
        m = new FixedMetadataValue(plugin, task);
        p.setMetadata(METADATA_KEY, m);
    }
}
