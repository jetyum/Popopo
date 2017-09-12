package online.popopo.popopo.domain;

import online.popopo.common.message.Caster;
import online.popopo.common.message.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class TeleportListener implements Listener {
    private static final String METADATA_KEY = "domain_switch";

    private final Plugin plugin;
    private final Formatter formatter;

    public TeleportListener(Plugin p, Formatter f) {
        this.plugin = p;
        this.formatter = f;

        Bukkit.getPluginManager().registerEvents(this, p);
    }

    private boolean canSwitch(Domain a, Domain b) {
        return a.available() && b.available();
    }

    private void removeFrag(Player p) {
        p.removeMetadata(METADATA_KEY, plugin);
    }

    private boolean hasFrag(Player p) {
        return p.hasMetadata(METADATA_KEY);
    }

    private void setFrag(Player p, boolean bool) {
        MetadataValue v;

        if (hasFrag(p)) removeFrag(p);
        v = new FixedMetadataValue(plugin, bool);
        p.setMetadata(METADATA_KEY, v);
    }

    private boolean getFrag(Player p) {
        MetadataValue v;

        v = p.getMetadata(METADATA_KEY).get(0);

        return v.asBoolean();
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        Domain from = new Domain(e.getFrom());
        Domain to = new Domain(e.getTo());

        if (from.equals(to) || hasFrag(p)) {
            e.setCancelled(hasFrag(p) && !getFrag(p));
        } else if (!canSwitch(from, to)) {
            Caster c = Caster.newFrom(formatter, p);

            c.bad("Error", "Can't switch domain!");
        } else {
            new Switcher(plugin, p, from, to) {
                @Override
                public void preProcess() {
                    setFrag(p, false);
                }

                @Override
                public void postProcess() {
                    setFrag(p, true);
                    p.teleport(e.getTo());
                    removeFrag(p);
                }
            }.switchDomain();
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Domain from = new Domain(p.getWorld());
        Domain to = new Domain(e.getRespawnLocation());

        if (canSwitch(from, to)) {
            Location l = from.getSpawnLocation();

            e.setRespawnLocation(l);
        }
    }
}
