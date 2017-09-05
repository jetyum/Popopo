package online.popopo.popopo.world;

import online.popopo.common.config.Configurable;
import online.popopo.common.config.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class WorldSetter implements Configurable {
    @Parameter("")
    private MemorySection section;

    private Set<WorldSetting> getSettings() {
        Set<WorldSetting> settings = new HashSet<>();

        if (section != null) {
            Map map = section.getValues(false);

            for (Object o : map.values()) {
                MemorySection s = (MemorySection) o;

                settings.add(new WorldSetting(s));
            }
        }

        return settings;
    }

    public boolean initWorlds(JavaPlugin p) {
        Set<World> lobbys = new HashSet<>();

        for (WorldSetting s : getSettings()) {
            WorldCreator c = s.generateCreator();
            World w = c.createWorld();

            if (w == null) {
                return false;
            } else if (s.hasLobbyWorld()) {
                if (s.getLobbyWorld()) {
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

    @Override
    public String getSectionName() {
        return "worlds";
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
