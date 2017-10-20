package online.popopo.api.io.yaml;

import online.popopo.api.io.Config;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YamlConfig implements Config {
    private final FileConfiguration config;

    public YamlConfig() {
        this.config = new YamlConfiguration();
    }

    @Override
    public void load(InputStream in) {
        try {
            config.load(new InputStreamReader(in));
        } catch (InvalidConfigurationException
                | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean contains(String key) {
        return config.contains(key);
    }

    @Override
    public void set(String key, Object value) {
        try {
            Class<?> t = value.getClass();

            if (t.isEnum()) {
                value = t.getMethod("name").invoke(value);
                value = ((String) value).toLowerCase();
            }

            config.set(key, value);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
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
    public Object get(String key, Class<?> c) {
        try {
            Object o = config.get(key);

            if (c.isEnum() && o instanceof String) {
                String n = ((String) o).toUpperCase();
                Method m;

                m = c.getMethod("valueOf", String.class);
                o = m.invoke(null, n);
            }

            return convert(o);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
