package online.popopo.common.config;

import com.google.common.io.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ConfigIO {
    private <V> V newV(Class<V> c) {
        try {
            return c.newInstance();
        } catch (IllegalAccessException
                | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public <V> V read(Config c, InputStream i, V v) {
        return new Serializer<V>(c).deserialize(i, v);
    }

    public <V> V read(Config c, File f, V v) {
        try {
            if (!f.exists()) return v;

            return read(c, new FileInputStream(f), v);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <V> V readYaml(File f, V v) {
        return read(new YamlConfig(), f, v);
    }

    public <V> V readYaml(File f, Class<V> c) {
        return readYaml(f, newV(c));
    }

    public <V> V readGzip(InputStream i, V v) {
        return read(new GzipConfig(), i, v);
    }

    public <V> V readGzip(InputStream i, Class<V> c) {
        return readGzip(i, newV(c));
    }

    public <V> V readGzip(File f, V v) {
        return read(new GzipConfig(), f, v);
    }

    public <V> V readGzip(File f, Class<V> c) {
        return readGzip(f, newV(c));
    }

    public <V> void write(Config c, File f, V v) {
        try {
            if (!f.exists()) Files.createParentDirs(f);

            new Serializer<V>(c).serialize(f, v);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <V> void writeYaml(File f, V v) {
        write(new YamlConfig(), f, v);
    }

    public <V> void writeGzip(File f, V v) {
        write(new GzipConfig(), f, v);
    }

    public <V> Map<String, V> readYamls(File d, Class<V> c) {
        Map<String, V> map = new HashMap<>();
        File[] files = d.listFiles();

        if (files != null) {
            for (File f : files) {
                String full = f.getName();
                int dot = full.lastIndexOf(".");
                String name = full.substring(0, dot);

                map.put(name, readYaml(f, c));
            }
        }

        return map;
    }
}
