package online.popopo.popopo.world;

import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.MemorySection;

public class WorldSetting {
    private static final String ENVIRONMENT = "environment";
    private static final String SEED = "seed";
    private static final String STRUCTURES = "structures";
    private static final String WORLD_PRESET = "world_preset";
    private static final String WORLD_TYPE = "world_type";
    private static final String LOBBY_WORLD = "lobby_world";

    private final MemorySection section;

    public WorldSetting(MemorySection s) {
        this.section = s;
    }

    private Environment getEnvironment() {
        String n = section.getString(ENVIRONMENT);

        try {
            return Environment.valueOf(n.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    private WorldType getWorldType() {
        String n = section.getString(WORLD_TYPE);

        return WorldType.getByName(n);
    }

    public boolean hasLobbyWorld() {
        return section.contains(LOBBY_WORLD);
    }

    public boolean getLobbyWorld() {
        return section.getBoolean(LOBBY_WORLD);
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
