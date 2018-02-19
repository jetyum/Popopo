package online.popopo.popopo.world;

import online.popopo.api.io.Name;
import online.popopo.api.io.Inject;
import org.bukkit.Difficulty;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class Info {
    @Name
    private String name;

    @Inject(key = "environment")
    private Environment environment = null;

    @Inject(key = "seed")
    private Long seed = null;

    @Inject(key = "structures")
    private Boolean structures = null;

    @Inject(key = "world_preset")
    private String preset = null;

    @Inject(key = "world_type")
    private WorldType type = null;

    @Inject(key = "lobby_world")
    private boolean lobby = false;

    @Inject(key = "difficulty")
    private Difficulty difficulty = Difficulty.EASY;

    @Inject(key = "pvp")
    private boolean pvp = true;

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

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public boolean getPVP() {
        return pvp;
    }
}
