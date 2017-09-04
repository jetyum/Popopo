package online.popopo.popopo.world;

import online.popopo.popopo.world.MultiWorld.WorldConfig;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldLoader {
    private final MultiWorld worlds;

    private World lobby;

    public WorldLoader(JavaPlugin p, MultiWorld w) {
        this.worlds = w;

        Listener l = new PlayerJoinListener();

        Bukkit.getPluginManager().registerEvents(l, p);
    }

    private boolean loadWorld(String name) {
        WorldCreator creator = WorldCreator.name(name);
        WorldConfig config = worlds.getConfig(name);

        if (config.hasEnvironment()) {
            Environment e = config.getEnvironment();

            if (e != null) {
                creator.environment(e);
            } else {
                return false;
            }
        }

        if (config.hasSeed()) {
            long seed = config.getSeed();

            creator.seed(seed);
        }

        if (config.hasStructures()) {
            boolean structures = config.getStructures();

            creator.generateStructures(structures);
        }

        if (config.hasWorldPreset()) {
            String worldPreset = config.getWorldPreset();

            creator.generatorSettings(worldPreset);
        }

        if (config.hasWorldType()) {
            WorldType t = config.getWorldType();

            if (t != null) {
                creator.type(t);
            } else {
                return false;
            }
        }

        World w = creator.createWorld();

        if (w != null) {
            if (config.hasLobbyWorld()) {
                if (config.getLobbyWorld()) {
                    lobby = w;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean loadWorlds() {
        for (String n : worlds.getNames()) {
            if (!loadWorld(n)) {
                return false;
            }
        }

        return true;
    }

    private class PlayerJoinListener implements Listener {
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent e) {
            Player p = e.getPlayer();

            if (!p.hasPlayedBefore() && lobby != null) {
                p.teleport(lobby.getSpawnLocation());
            }
        }
    }
}
