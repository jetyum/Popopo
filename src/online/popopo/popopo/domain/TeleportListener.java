package online.popopo.popopo.domain;

import online.popopo.common.message.Caster;
import online.popopo.common.message.SenderCaster.PlayerCaster;
import online.popopo.common.message.Theme;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleportListener implements Listener {
    private final JavaPlugin plugin;
    private final Theme theme;

    public TeleportListener(JavaPlugin p, Theme t) {
        this.plugin = p;
        this.theme = t;

        Bukkit.getPluginManager().registerEvents(this, p);
    }

    private boolean canSwitch(Domain a, Domain b) {
        return a.available() && b.available();
    }

    private void setFrag(Player p) {
        MetadataValue v;

        v = new FixedMetadataValue(this.plugin, true);
        p.setMetadata("domain_switch", v);
    }

    private void removeFrag(Player p) {
        p.removeMetadata("domain_switch", this.plugin);
    }

    private boolean hasFrag(Player p) {
        return p.hasMetadata("domain_switch");
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        Domain from = new Domain(e.getFrom());
        Domain to = new Domain(e.getTo());

        if (from.equals(to) || hasFrag(p)) {
            return;
        } else if (!canSwitch(from, to)) {
            Caster c = new PlayerCaster(this.theme, p);

            c.bad("Error", "Can't switch domain!");

            return;
        }

        new Switcher(this.plugin, p, from, to) {
            @Override
            public void preProcess() {
                setFrag(p);
            }

            @Override
            public void postProcess() {
                p.teleport(e.getTo());
                removeFrag(p);
            }
        }.switchDomain();

        e.setCancelled(true);
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
