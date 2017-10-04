package online.popopo.api.config;

import com.google.common.io.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class Serializer<V> {
    private final Config config;

    public Serializer(Config c) {
        this.config = c;
    }

    private void injectInto(V v, String k, Field f) {
        try {
            if (!config.contains(k)) return;

            f.setAccessible(true);
            f.set(v, config.get(k, f.getType()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void extractFrom(V v, String k, Field f) {
        try {
            f.setAccessible(true);
            config.set(k, f.get(v));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public V read(InputStream in, V target) {
        Class type = target.getClass();

        config.load(in);

        for (Field f : type.getDeclaredFields()) {
            for (Annotation a : f.getAnnotations()) {
                if (!(a instanceof Property)) {
                    continue;
                }

                String k = ((Property) a).key();

                injectInto(target, k, f);
            }
        }

        return target;
    }

    public V read(File file, V target) {
        try {
            if (!file.exists()) return target;

            InputStream in;

            in = new FileInputStream(file);

            return read(in, target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(File file, V value) {
        try {
            if (!file.exists()) {
                Files.createParentDirs(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Class type = value.getClass();

        for (Field f : type.getDeclaredFields()) {
            for (Annotation a : f.getAnnotations()) {
                if (!(a instanceof Property)) {
                    continue;
                }

                String k = ((Property) a).key();

                extractFrom(value, k, f);
            }
        }

        config.save(file);
    }
}
