package online.popopo.common.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        } catch (IllegalAccessException
                | NoSuchMethodException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
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
            } else if (o instanceof MemorySection) {
                o = ((MemorySection) o).getValues(false);
            }

            return o;
        } catch (IllegalAccessException
                | NoSuchMethodException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
