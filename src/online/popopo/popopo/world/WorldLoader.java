package online.popopo.popopo.world;

import online.popopo.api.MainBase;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

public class WorldLoader {
    public void load(MainBase p, Map<String, WorldInfo> m) {
        Set<World> lobbys = new HashSet<>();

        for (WorldInfo i : m.values()) {
            WorldCreator c = i.worldCreator();
            World w = c.createWorld();

            w.setDifficulty(i.getDifficulty());
            w.setPVP(i.getPVP());

            if (i.isLobbyWorld()) lobbys.add(w);
        }

        if (!lobbys.isEmpty()) {
            p.register(new LobbyListener(lobbys));
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
