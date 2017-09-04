package online.popopo.popopo.world;

import online.popopo.popopo.world.MultiWorld.WorldConfig;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class WorldLoader {
    private final MultiWorld worlds;

    public WorldLoader(MultiWorld w) {
        this.worlds = w;
    }

    private boolean loadWorld(String name) {
        WorldCreator c = WorldCreator.name(name);
        WorldConfig w = worlds.getConfig(name);

        if (w.hasEnvironment()) {
            Environment e = w.getEnvironment();

            if (e != null) {
                c.environment(e);
            } else {
                return false;
            }
        }

        if (w.hasSeed()) {
            long seed = w.getSeed();

            c.seed(seed);
        }

        if (w.hasStructures()) {
            boolean structures = w.getStructures();

            c.generateStructures(structures);
        }

        if (w.hasWorldPreset()) {
            String worldPreset = w.getWorldPreset();

            c.generatorSettings(worldPreset);
        }

        if (w.hasWorldType()) {
            WorldType t = w.getWorldType();

            if (t != null) {
                c.type(t);
            } else {
                return false;
            }
        }

        return c.createWorld() != null;
    }

    public boolean loadWorlds() {
        for (String n : worlds.getNames()) {
            if (!loadWorld(n)) {
                return false;
            }
        }

        return true;
    }
}
