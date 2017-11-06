package online.popopo.api.io;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Injector {
    private String getKeyIfHas(Field f) {
        for (Annotation a : f.getAnnotations()) {
            if (a instanceof Property) {
                return ((Property) a).key();
            }
        }

        return null;
    }

    public <T> void inject(Tree from, T to) {
        Class t = to.getClass();

        for (Field f : t.getDeclaredFields()) {
            String k = getKeyIfHas(f);

            if (k != null && from.contains(k)) {
                try {
                    boolean flag = f.isAccessible();

                    f.setAccessible(true);
                    f.set(to, from.get(k, f.getType()));
                    f.setAccessible(flag);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public <T> void inject(T from, Tree to) {
        Class t = from.getClass();

        for (Field f : t.getDeclaredFields()) {
            String k = getKeyIfHas(f);

            if (k != null) {
                try {
                    boolean flag = f.isAccessible();

                    f.setAccessible(true);
                    to.set(k, f.get(from));
                    f.setAccessible(flag);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private <T> T newInstance(Class<T> t, String name) {
        try {
            T v = t.newInstance();

            for (Field f : t.getDeclaredFields()) {
                if (f.isAnnotationPresent(Name.class)) {
                    boolean flag = f.isAccessible();

                    f.setAccessible(true);
                    f.set(v, name);
                    f.setAccessible(flag);
                }
            }

            return v;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void inject(Tree from, Map<String, T> to,
                           Class<T> t) {
        for (String k : from.keys()) {
            T v = newInstance(t, k);

            inject(from.child(k), v);
            to.put(k, v);
        }
    }

    public <T> void inject(Map<String, T> from, Tree to) {
        for (String k : from.keySet()) {
            Map<String, Object> v = new HashMap<>();

            to.set(k, v);
            inject(from.get(k), to.child(k));
        }
    }
}
