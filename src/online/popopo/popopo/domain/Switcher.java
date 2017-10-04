package online.popopo.popopo.domain;

import online.popopo.api.nbt.NBT;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class Switcher {
    private static final String METADATA_KEY = "domain_switch";

    static final int NONE = 0;
    static final int SWITCHING = 1;
    static final int SWITCHED = 2;

    private final Plugin plugin;

    Switcher(Plugin plugin) {
        this.plugin = plugin;
    }

    private void resetPotionEffects(Player p) {
        p.getActivePotionEffects().forEach(e -> {
            p.removePotionEffect(e.getType());
        });
    }

    private void fixPlayerData(Player p, PlayerData data)
            throws IOException {
        NBT t = data.readData();
        Map<String, NBT> m = t.getCompound();
        UUID uuid = p.getWorld().getUID();
        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();

        m.get("WorldUUIDMost").setValue(most);
        m.get("WorldUUIDLeast").setValue(least);
        m.get("PortalCooldown").setValue(900);
        t.setValue(m);
        data.writeData(t);
    }

    private void setState(Player p, int state) {
        if (!p.hasMetadata(METADATA_KEY)) {
            p.removeMetadata(METADATA_KEY, plugin);
        }

        MetadataValue v;

        v = new FixedMetadataValue(plugin, state);
        p.setMetadata(METADATA_KEY, v);
    }

    int getState(Player p) {
        if (p.hasMetadata(METADATA_KEY)) {
            List<MetadataValue> list;

            list = p.getMetadata(METADATA_KEY);

            if (!list.isEmpty()) {
                return list.get(0).asInt();
            }
        }

        return NONE;
    }

    boolean switchTo(Player p, Domain from, Domain to,
                     Runnable pre, Runnable post) {
        BukkitScheduler s = Bukkit.getScheduler();

        setState(p, SWITCHING);
        s.runTaskAsynchronously(plugin, () -> {
            Domain main = Domain.getMain();
            PlayerData data = new PlayerData(p, main);

            try {
                pre.run();
                p.saveData();
                resetPotionEffects(p);
                new PlayerData(p, from).swapData(data);
                new PlayerData(p, to).swapData(data);
                fixPlayerData(p, data);

                s.runTask(plugin, () -> {
                    p.loadData();
                    setState(p, SWITCHED);
                    post.run();
                    setState(p, NONE);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return true;
    }
}
