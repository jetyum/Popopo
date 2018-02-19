package online.popopo.popopo.world;

import online.popopo.api.function.Variable;
import online.popopo.api.function.listener.ListenerManager;
import online.popopo.api.io.Injector;
import online.popopo.api.io.tree.Config;
import online.popopo.api.function.Function;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.*;

public class WorldFunc extends Function implements Listener {
    @Variable
    private Plugin plugin;
    @Variable
    private ListenerManager listenerManager;

    private final Map<String, WorldInfo> worlds;
    private final Set<World> lobbys;

    public WorldFunc() {
        this.worlds = new HashMap<>();
        this.lobbys = new HashSet<>();
    }

    @Override
    public void load() {
        try {
            Config c = new Config(plugin, "world.yml");
            c.load();
            Injector.inject(c, worlds, WorldInfo.class);
        } catch (IOException e) {
            plugin.getLogger().info("World wasn't loaded");
        }
    }

    @Override
    public void enable() {
        for (WorldInfo i : worlds.values()) {
            WorldCreator c = i.worldCreator();
            World w = c.createWorld();

            w.setDifficulty(i.getDifficulty());
            w.setPVP(i.getPVP());

            if (i.isLobbyWorld()) lobbys.add(w);
        }

        if (!lobbys.isEmpty()) {
            listenerManager.register(this);
        }
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
