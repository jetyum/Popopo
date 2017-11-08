package online.popopo.api.io.tree;

import java.lang.reflect.Method;

public class EnumUtils {
    public static Object getEnum(Class<?> t, String name) {
        try {
            if (!t.isEnum()) return null;

            final String n = "valueOf";
            Method m = t.getMethod(n, String.class);

            return m.invoke(null, name);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    public static String getName(Object enumValue) {
        if (enumValue.getClass().isEnum()) {
            return enumValue.toString().toLowerCase();
        } else {
            return null;
        }
    }
}
