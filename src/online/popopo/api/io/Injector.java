package online.popopo.api.io;

import online.popopo.api.io.tree.Tree;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Injector {
    private static String getKeyIfHas(Field f) {
        if (f.isAnnotationPresent(Inject.class)) {
            return f.getAnnotation(Inject.class).key();
        } else {
            return null;
        }
    }

    public static <T> void inject(Tree from, T to) {
        Class t = to.getClass();

        for (Field f : t.getDeclaredFields()) {
            String k = getKeyIfHas(f);

            if (k != null && from.contains(k)) try {
                boolean flag = f.isAccessible();

                f.setAccessible(true);
                f.set(to, from.get(k, f.getType()));
                f.setAccessible(flag);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> void inject(T from, Tree to) {
        Class t = from.getClass();

        for (Field f : t.getDeclaredFields()) {
            String k = getKeyIfHas(f);

            if (k != null) try {
                boolean flag = f.isAccessible();

                f.setAccessible(true);
                to.set(k, f.get(from));
                f.setAccessible(flag);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static <T> T newInstance(Class<T> t,
                                     String name) {
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

    public static <T> void inject(Tree from,
                                  Map<String, T> to,
                                  Class<T> t) {
        for (String k : from.keys()) {
            T v = newInstance(t, k);

            inject(from.child(k), v);
            to.put(k, v);
        }
    }

    public static <T> void inject(Map<String, T> from,
                                  Tree to) {
        for (String k : from.keySet()) {
            Map<String, Object> v = new HashMap<>();

            to.set(k, v);
            inject(from.get(k), to.child(k));
        }
    }
}
