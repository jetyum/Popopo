package online.popopo.popopo.world;

import online.popopo.common.config.Configurable;
import online.popopo.common.config.Parameter;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.configuration.MemorySection;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MultiWorld implements Configurable {
    @Parameter("")
    private MemorySection config = null;

    public Set<String> getNames() {
        if (this.config == null) {
            return new HashSet<>();
        } else {
            return this.config.getKeys(false);
        }
    }

    public WorldConfig getConfig(String name) {
        if (this.config == null) {
            return null;
        } else if (!this.config.contains(name)) {
            return null;
        } else {
            Map map = this.config.getValues(false);
            Object o = map.get(name);

            return new WorldConfig((MemorySection) o);
        }
    }

    public static class WorldConfig {
        private final MemorySection c;

        private WorldConfig(MemorySection c) {
            this.c = c;
        }

        public boolean hasEnvironment() {
            return this.c.contains("environment");
        }

        public Environment getEnvironment() {
            String n = this.c.getString("environment")
                    .toUpperCase();

            try {
                return Environment.valueOf(n);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        public boolean hasSeed() {
            return this.c.contains("seed");
        }

        public long getSeed() {
            return this.c.getLong("seed");
        }

        public boolean hasWorldPreset() {
            return this.c.contains("world_preset");
        }

        public String getWorldPreset() {
            return this.c.getString("world_preset");
        }

        public boolean hasStructures() {
            return this.c.contains("structures");
        }

        public boolean getStructures() {
            return this.c.getBoolean("structures");
        }

        public boolean hasWorldType() {
            return this.c.contains("world_type");
        }

        public WorldType getWorldType() {
            String n = this.c.getString("world_type");

            return WorldType.getByName(n);
        }
    }

    @Override
    public String getSectionName() {
        return "worlds";
    }
}
