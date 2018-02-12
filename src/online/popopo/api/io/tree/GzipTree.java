package online.popopo.api.io.tree;

import org.apache.commons.lang3.Validate;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipTree extends Tree {
    private Map<String, Object> data;

    public GzipTree() {
        super(null);
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

    public void save(OutputStream out) throws IOException {
        GZIPOutputStream zipOut;
        ObjectOutputStream objOut;

        zipOut = new GZIPOutputStream(out);
        objOut = new ObjectOutputStream(zipOut);

        objOut.writeObject(data);
        objOut.close();
    }

    @Override
    public void set(String key, Object v) {
        Validate.notEmpty(key);

        Class<?> t = v.getClass();

        if (t.isEnum()) {
            v = EnumUtils.getName(v);
        }

        data.put(key, v);
    }

    @Override
    public <T> Object get(String key, Class<T> t) {
        if (key.isEmpty()) return data;

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

    @Override
    public boolean contains(String key) {
        return data.containsKey(key) || key.isEmpty();
    }
}
