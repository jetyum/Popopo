package online.popopo.popopo.common.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class PluginConfig implements Config {
    public boolean injectInto(Configurable i) {
        String section = i.getSectionName();

        for (Field f : i.getClass().getDeclaredFields()) {
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
                f.set(i, o);
            } catch (IllegalAccessException
                    | NoSuchMethodException
                    | InvocationTargetException e) {
                return false;
            }
        }

        return true;
    }

    public boolean extractFrom(Configurable i) {
        String section = i.getSectionName();

        for (Field f : i.getClass().getDeclaredFields()) {
            if (!f.isAnnotationPresent(Parameter.class)) {
                continue;
            }

            Parameter v = f.getAnnotation(Parameter.class);

            try {
                f.setAccessible(true);

                Object o = f.get(i);
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
}
