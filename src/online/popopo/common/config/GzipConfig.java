package online.popopo.common.config;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipConfig implements Config {
    private Map<String, Object> config;

    public GzipConfig() {
        this.config = new HashMap<>();
    }

    @Override
    public void load(InputStream in) {
        try {
            GZIPInputStream zipIn;
            ObjectInputStream objIn;

            zipIn = new GZIPInputStream(in);
            objIn = new ObjectInputStream(zipIn);

            Object o = objIn.readObject();

            config = config.getClass().cast(o);
            objIn.close();
        } catch (ClassNotFoundException
                | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(File file) {
        try {
            FileOutputStream fileOut;
            GZIPOutputStream zipOut;
            ObjectOutputStream objOut;

            fileOut = new FileOutputStream(file);
            zipOut = new GZIPOutputStream(fileOut);
            objOut = new ObjectOutputStream(zipOut);

            objOut.writeObject(config);
            objOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean contains(String key) {
        return config.containsKey(key);
    }

    @Override
    public void set(String key, Object value) {
        config.put(key, value);
    }

    @Override
    public Object get(String key, Class<?> c) {
        return config.get(key);
    }
}
