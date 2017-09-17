package online.popopo.common.config;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ConfigIO {
    private final Plugin plugin;

    public ConfigIO(Plugin p) {
        this.plugin = p;
    }

    private <V> V newV(Class<V> c) {
        try {
            return c.newInstance();
        } catch (IllegalAccessException
                | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private Config getConfig(String name) {
        int dot = name.lastIndexOf(".");
        String suffix = name.substring(dot + 1);

        switch (suffix) {
            case "yml":
                return new YamlConfig();
            default:
                return new GzipConfig();
        }
    }

    public <V> V read(String path, V v) {
        File f = new File(plugin.getDataFolder(), path);
        Config c = getConfig(path);

        return new Serializer<V>(c).read(f, v);
    }

    public <V> V read(String path, Class<V> c) {
        return read(path, newV(c));
    }

    public <V> V readResource(String name, V v) {
        InputStream i = plugin.getResource(name);
        Config c = getConfig(name);

        return new Serializer<V>(c).read(i, v);
    }

    public <V> V readResource(String name, Class<V> c) {
        return readResource(name, newV(c));
    }

    public <V> void write(String path, V v) {
        File f = new File(plugin.getDataFolder(), path);
        Config c = getConfig(path);

        new Serializer<V>(c).write(f, v);
    }

    public <V> Map<String, V> readDir(String path, Class<V> c) {
        Map<String, V> map = new HashMap<>();
        File d = new File(plugin.getDataFolder(), path);
        File[] files = d.listFiles();

        if (files != null) {
            for (File f : files) {
                String name = f.getName();
                String full = path + "/" + name;
                int dot = name.lastIndexOf(".");
                String key = name.substring(0, dot);

                map.put(key, read(full, c));
            }
        }

        return map;
    }
}
