package online.popopo.popopo.world;

import online.popopo.api.config.Property;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class WorldInfo {
    @Property(key = "name")
    private String name;

    @Property(key = "environment")
    private Environment environment = null;

    @Property(key = "seed")
    private Long seed = null;

    @Property(key = "structures")
    private Boolean structures = null;

    @Property(key = "world_preset")
    private String preset = null;

    @Property(key = "world_type")
    private WorldType type = null;

    @Property(key = "lobby_world")
    private boolean lobby = false;

    public WorldCreator worldCreator() {
        WorldCreator c = WorldCreator.name(name);

        if (environment != null) {
            c.environment(environment);
        }

        if (seed != null) {
            c.seed(seed);
        }

        if (structures != null) {
            c.generateStructures(structures);
        }

        if (preset != null) {
            c.generatorSettings(preset);
        }

        if (type != null) {
            c.type(type);
        }

        return c;
    }

    public boolean isLobbyWorld() {
        return lobby;
    }
}
