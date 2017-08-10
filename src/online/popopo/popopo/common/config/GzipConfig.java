package online.popopo.popopo.common.config;

import com.google.common.io.Files;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipConfig extends Config {
    private final String filepath;
    private Map<String, Map<String, Object>> table;

    public GzipConfig(String filepath) {
        this.filepath = filepath;
        this.table = new HashMap<>();
    }

    @Override
    public boolean load() {
        try {
            File file = new File(this.filepath);
            InputStream fileIn, gzipIn;
            ObjectInputStream in;

            if (!file.exists()) {
                return false;
            }

            fileIn = new FileInputStream(file);
            gzipIn = new GZIPInputStream(fileIn);
            in = new ObjectInputStream(gzipIn);

            Object data = in.readObject();

            this.table = this.table.getClass().cast(data);
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
            File file = new File(this.filepath);
            OutputStream fileOut, gzipOut;
            ObjectOutputStream out;

            if (!file.exists()) {
                Files.createParentDirs(file);
            }

            fileOut = new FileOutputStream(file);
            gzipOut = new GZIPOutputStream(fileOut);
            out = new ObjectOutputStream(gzipOut);

            out.writeObject(this.table);
            out.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean contain(String section, String key) {
        return this.table.containsKey(section)
                || table.get(section).containsKey(key);
    }

    @Override
    public void set(String section, String key, Object value) {
        if (!this.table.containsKey(section)) {
            this.table.put(section, new HashMap<>());
        }

        this.table.get(section).put(key, value);
    }

    @Override
    public Object get(String section, String key) {
        return this.table.get(section).get(key);
    }
}
