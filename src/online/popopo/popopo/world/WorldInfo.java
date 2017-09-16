package online.popopo.popopo.world;

import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.MemorySection;

public class WorldBean {
    private static final String ENVIRONMENT = "environment";
    private static final String SEED = "seed";
    private static final String STRUCTURES = "structures";
    private static final String WORLD_PRESET = "world_preset";
    private static final String WORLD_TYPE = "world_type";
    private static final String LOBBY_WORLD = "lobby_world";

    private String name = null;
    private Environment environment = null;
    private Long seed = null;
    private Boolean structures = null;
    private String preset = null;
    private WorldType type = null;
    private Boolean lobby = null;

    public boolean hasLobbyWorld() {
        return lobby != null;
    }

    public boolean getLobbyWorld() {
        return lobby;
    }

    public WorldCreator generateCreator() {
        String name = section.getName();
        WorldCreator c = WorldCreator.name(name);

        if (section.contains(ENVIRONMENT)) {
            c.environment(getEnvironment());
        }

        if (section.contains(SEED)) {
            c.seed(section.getLong(SEED));
        }

        if (section.contains(STRUCTURES)) {
            c.generateStructures(
                    section.getBoolean(STRUCTURES));
        }

        if (section.contains(WORLD_PRESET)) {
            c.generatorSettings(
                    section.getString(WORLD_PRESET));
        }

        if (section.contains(WORLD_TYPE)) {
            c.type(getWorldType());
        }

        return c;
    }
}
