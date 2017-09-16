package online.popopo.common.config;

import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Serializer<V> {
    private final Config config;

    public Serializer(Config c) {
        this.config = c;
    }

    private void injectInto(V v, String k, Field f) {
        try {
            if (!config.contains(k)) return;

            Object o = config.get(k);
            Class<?> t = f.getType();

            if (t.isEnum() && o instanceof String) {
                String n = ((String) o).toUpperCase();
                Method m;

                m = t.getMethod("valueOf", String.class);
                o = m.invoke(null, n);
            }

            f.setAccessible(true);
            f.set(v, o);
        } catch (IllegalAccessException
                | NoSuchMethodException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void extractFrom(V v, String k, Field f) {
        try {
            f.setAccessible(true);

            Object o = f.get(v);
            Class<?> t = f.getType();

            if (t.isEnum()) {
                o = t.getMethod("name").invoke(o);
                o = ((String) o).toLowerCase();
            }

            config.set(k, o);
        } catch (IllegalAccessException
                | NoSuchMethodException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public V deserialize(InputStream in, V target) {
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

    public void serialize(File file, V value) {
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
