package online.popopo.common.config;

import com.google.common.io.Files;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipConfig extends Config {
    private final String path;
    private Map<String, Map<String, Object>> table;

    public GzipConfig(String path) {
        this.path = path;
        this.table = new HashMap<>();
    }

    public static GzipConfig newFrom(Plugin p, String path) throws IOException {
        String dir = p.getDataFolder().getAbsolutePath();
        String abs = dir + "/" + path;

        if (!new File(abs).exists()) {
            p.saveResource(path, false);
        }

        return new GzipConfig(abs);
    }

    @Override
    public boolean load() {
        try {
            File file = new File(path);
            InputStream fileIn, gzipIn;
            ObjectInputStream in;

            if (!new File(path).exists()) {
                return false;
            }

            fileIn = new FileInputStream(file);
            gzipIn = new GZIPInputStream(fileIn);
            in = new ObjectInputStream(gzipIn);
            Object data = in.readObject();

            table = table.getClass().cast(data);
            in.close();

            return true;
        } catch (ClassNotFoundException
                | IOException e) {
            return false;
        }
    }

    @Override
    public boolean save() {
        try {
            File file = new File(path);
            OutputStream fileOut, gzipOut;
            ObjectOutputStream out;

            if (!file.exists()) {
                Files.createParentDirs(file);
            }

            fileOut = new FileOutputStream(file);
            gzipOut = new GZIPOutputStream(fileOut);
            out = new ObjectOutputStream(gzipOut);

            out.writeObject(table);
            out.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean contain(String section, String key) {
        return table.containsKey(section)
                && table.get(section).containsKey(key);
    }

    @Override
    public void set(String section, String key, Object v) {
        if (!table.containsKey(section)) {
            table.put(section, new HashMap<>());
        }

        table.get(section).put(key, v);
    }

    @Override
    public Object get(String section, String key) {
        return table.get(section).get(key);
    }
}
