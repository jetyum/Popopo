package online.popopo.popopo.domain;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class TeleportListener implements Listener {
    private static final Set<String> users;

    static {
        users = new CopyOnWriteArraySet<>();
    }

    private final JavaPlugin plugin;

    public TeleportListener(JavaPlugin p) {
        this.plugin = p;

        Bukkit.getPluginManager().registerEvents(this, p);
    }

    private void setFrag(Player p) {
        users.add(p.getName());
    }

    private void removeFrag(Player p) {
        users.remove(p.getName());
    }

    private boolean hasFrag(Player p) {
        return users.contains(p.getName());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        Domain from = new Domain(e.getFrom());
        Domain to = new Domain(e.getTo());
        boolean able = from.available() && to.available();

        if (!from.equals(to) && able && !hasFrag(p)) {

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
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Domain d = new Domain(p.getWorld());

        e.setRespawnLocation(d.getSpawnLocation());
    }
}