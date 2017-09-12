package online.popopo.common.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Config {
    public boolean injectInto(Configurable c) {
        String section = c.getSectionName();

        for (Field f : c.getClass().getDeclaredFields()) {
            if (!f.isAnnotationPresent(Parameter.class)) {
                continue;
            }

            Parameter v = f.getAnnotation(Parameter.class);

            if (!contain(section, v.value())) {
                continue;
            }

            try {
                Object o = get(section, v.value());
                Class<?> t = f.getType();

                if (t.isEnum() && o instanceof String) {
                    String n = ((String) o).toUpperCase();
                    Method m = t.getMethod(
                            "valueOf", String.class);

                    o = m.invoke(null, n);
                }

                f.setAccessible(true);
                f.set(c, o);
            } catch (IllegalAccessException
                    | NoSuchMethodException
                    | InvocationTargetException e) {
                return false;
            }
        }

        return true;
    }

    public boolean extractFrom(Configurable c) {
        String section = c.getSectionName();

        for (Field f : c.getClass().getDeclaredFields()) {
            if (!f.isAnnotationPresent(Parameter.class)) {
                continue;
            }

            Parameter v = f.getAnnotation(Parameter.class);

            try {
                f.setAccessible(true);

                Object o = f.get(c);
                Class<?> t = f.getType();

                if (t.isEnum()) {
                    o = t.getMethod("name").invoke(o);
                    o = ((String) o).toLowerCase();
                }

                set(section, v.value(), o);
            } catch (IllegalAccessException
                    | NoSuchMethodException
                    | InvocationTargetException e) {
                return false;
            }
        }

        return true;
    }

    abstract boolean load();

    abstract boolean save();

    abstract boolean contain(String section, String key);

    abstract void set(String section, String key, Object v);

    abstract Object get(String section, String key);
}
