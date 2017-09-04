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
        if (config == null) {
            return new HashSet<>();
        } else {
            return config.getKeys(false);
        }
    }

    public WorldConfig getConfig(String name) {
        if (config == null) {
            return null;
        } else if (!config.contains(name)) {
            return null;
        } else {
            Map map = config.getValues(false);
            Object o = map.get(name);

            return new WorldConfig((MemorySection) o);
        }
    }

    public class WorldConfig {
        private final MemorySection section;

        private WorldConfig(MemorySection c) {
            this.section = c;
        }

        public boolean hasEnvironment() {
            return section.contains("environment");
        }

        public Environment getEnvironment() {
            String n = section.getString("environment")
                    .toUpperCase();

            try {
                return Environment.valueOf(n);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        public boolean hasSeed() {
            return section.contains("seed");
        }

        public long getSeed() {
            return section.getLong("seed");
        }

        public boolean hasWorldPreset() {
            return section.contains("world_preset");
        }

        public String getWorldPreset() {
            return section.getString("world_preset");
        }

        public boolean hasStructures() {
            return section.contains("structures");
        }

        public boolean getStructures() {
            return section.getBoolean("structures");
        }

        public boolean hasWorldType() {
            return section.contains("world_type");
        }

        public WorldType getWorldType() {
            String n = section.getString("world_type");

            return WorldType.getByName(n);
        }
    }

    @Override
    public String getSectionName() {
        return "worlds";
    }
}
