package online.popopo.popopo.world;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.Map.Entry;

public class WorldSetter {
    public boolean init(Plugin p, Map<String, WorldInfo> m) {
        Set<World> lobbys = new HashSet<>();

        for (Entry<String, WorldInfo> e : m.entrySet()) {
            String name = e.getKey();
            WorldInfo info = e.getValue();
            WorldCreator c = info.worldCreator(name);
            World w = c.createWorld();

            if (w == null) {
                return false;
            } else if (info.isLobbyWorld()) {
                if (info.isLobbyWorld()) {
                    lobbys.add(w);
                }
            }
        }

        if (!lobbys.isEmpty()) {
            Listener l = new LobbyListener(lobbys);

            Bukkit.getPluginManager().registerEvents(l, p);
        }

        return true;
    }

    private class LobbyListener implements Listener {
        private final Set<World> lobbys;

        private LobbyListener(Set<World> lobbys) {
            this.lobbys = lobbys;
        }

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent e) {
            Player p = e.getPlayer();

            if (!p.hasPlayedBefore() && !lobbys.isEmpty()) {
                Random r = new Random();
                int i = r.nextInt(lobbys.size());
                World w = (World) lobbys.toArray()[i];

                p.teleport(w.getSpawnLocation());
            }
        }
    }
}
