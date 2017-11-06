package online.popopo.api.io;

import com.google.common.io.Files;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Data extends Tree {
    private final File dataFolder;

    private Map<String, Object> data;

    public Data(File dataFolder) {
        super(null);
        this.dataFolder = dataFolder;
        this.data = new HashMap<>();
    }

    public void load(InputStream in) throws IOException {
        GZIPInputStream zipIn;
        ObjectInputStream objIn;

        zipIn = new GZIPInputStream(in);
        objIn = new ObjectInputStream(zipIn);

        try {
            Object o = objIn.readObject();

            data = data.getClass().cast(o);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        objIn.close();
    }

    public void load(String path) throws IOException {
        File f = new File(dataFolder, path);

        load(new FileInputStream(f));
    }

    public void save(OutputStream out) throws IOException {
        GZIPOutputStream zipOut;
        ObjectOutputStream objOut;

        zipOut = new GZIPOutputStream(out);
        objOut = new ObjectOutputStream(zipOut);

        objOut.writeObject(data);
        objOut.close();
    }

    public void save(String path) throws IOException {
        File f = new File(dataFolder, path);

        Files.createParentDirs(f);
        save(new FileOutputStream(f));
    }

    @Override
    public void set(String key, Object v) {
        Class<?> t = v.getClass();

        if (t.isEnum()) {
            v = EnumUtils.getName(v);
        }

        data.put(key, v);
    }

    @Override
    public <T> Object get(String key, Class<T> t) {
        Object o = data.get(key);

        if (t.isEnum() && o instanceof String) {
            String n = ((String) o).toUpperCase();

            o = EnumUtils.getEnum(t, n);
        }

        return o;
    }

    @Override
    public Set<String> keys() {
        return data.keySet();
    }
}
