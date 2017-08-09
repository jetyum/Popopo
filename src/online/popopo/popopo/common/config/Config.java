package online.popopo.popopo.common.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Config {
    private final YamlConfiguration config;

    public Config() {
        this.config = new YamlConfiguration();
    }

    public boolean load(String pathname) {
        try {
            this.config.load(pathname);

            return true;
        } catch (InvalidConfigurationException
                | IOException e) {
            return false;
        }
    }

    private boolean setTo(Configurable c, Field f, Object o) {
        try {
            Class<?> t = f.getType();

            if (t.isEnum() && o instanceof String) {
                String n = ((String) o).toUpperCase();
                Method m = t.getMethod("valueOf", String.class);

                o = m.invoke(null, n);
            }

            f.setAccessible(true);
            f.set(c, o);

            return true;
        } catch (IllegalAccessException
                | NoSuchMethodException
                | InvocationTargetException e) {
            return false;
        }
    }

    public boolean setTo(Configurable c) {
        for (Field f : c.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(Parameter.class)) {
                Parameter p = f.getAnnotation(Parameter.class);
                String s = c.getConfigName() + "." + p.value();

                if (this.config.isSet(s)) {
                    Object o = this.config.get(s);

                    if (!setTo(c, f, o)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
