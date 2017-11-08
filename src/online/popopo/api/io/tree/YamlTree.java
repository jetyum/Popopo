package online.popopo.api.io.tree;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class YamlTree extends Tree {
    private final FileConfiguration config;

    public YamlTree() {
        super(null);
        this.config = new YamlConfiguration();
    }

    public void load(File f) throws IOException {
        try {
            config.load(f);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save(File f) throws IOException {
        config.save(f);
    }

    @Override
    public void set(String key, Object v) {
        Class<?> t = v.getClass();

        if (t.isEnum()) {
            v = EnumUtils.getName(v);
        }

        config.set(key, v);
    }

    private Object convert(Object o) {
        if (o instanceof MemorySection) {
            MemorySection s = (MemorySection) o;
            Map<String, Object> m = s.getValues(false);

            m.replaceAll((k, v) -> convert(v));

            return m;
        } else if (o instanceof List) {
            List<?> l = (List<?>) o;
            List<Object> copy = new ArrayList<>();

            l.forEach(v -> copy.add(convert(v)));

            return copy;
        } else {
            return o;
        }
    }

    @Override
    public <T> Object get(String key, Class<T> t) {
        Object o = config.get(key);

        if (t.isEnum() && o instanceof String) {
            String n = ((String) o).toUpperCase();

            o = EnumUtils.getEnum(t, n);
        }

        return convert(o);
    }

    @Override
    public Set<String> keys() {
        return config.getKeys(false);
    }
}
