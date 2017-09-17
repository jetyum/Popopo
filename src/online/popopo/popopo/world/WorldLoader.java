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

public class WorldSetter {
    public void init(Plugin p, Map<String, WorldInfo> m) {
        Set<World> lobbys = new HashSet<>();

        for (WorldInfo i : m.values()) {
            WorldCreator c = i.worldCreator();
            World w = c.createWorld();

            if (i.isLobbyWorld()) lobbys.add(w);
        }

        if (!lobbys.isEmpty()) {
            Listener l = new LobbyListener(lobbys);

            Bukkit.getPluginManager().registerEvents(l, p);
        }
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
