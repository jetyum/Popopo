package online.popopo.popopo.domain;

import online.popopo.common.nbt.NBT;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.*;
import java.util.Map;
import java.util.UUID;

class Switcher {
    private static final String METADATA_KEY = "domain_switch";

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

    private void setState(Player p, SwitchState s) {
        MetadataValue v;

        if (!p.hasMetadata(METADATA_KEY)) {
            p.removeMetadata(METADATA_KEY, plugin);
        }

        v = new FixedMetadataValue(plugin, s);
        p.setMetadata(METADATA_KEY, v);
    }

    SwitchState getState(Player p) {
        if (!p.hasMetadata(METADATA_KEY)) {
            return SwitchState.NONE;
        } else {
            MetadataValue v;

            v = p.getMetadata(METADATA_KEY).get(0);

            return (SwitchState) v.value();
        }
    }

    boolean switchTo(Player p, Domain from, Domain to,
                     Runnable pre, Runnable post) {
        BukkitScheduler s = Bukkit.getScheduler();

        setState(p, SwitchState.SWITCHING);
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
                    setState(p, SwitchState.SWITCHED);
                    post.run();
                    setState(p, SwitchState.NONE);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return true;
    }

    enum SwitchState {SWITCHING, SWITCHED, NONE}
}
