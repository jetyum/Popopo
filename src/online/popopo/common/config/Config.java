package online.popopo.common.config;

import java.io.File;
import java.io.InputStream;

public interface Config {
    void load(InputStream in);

    void save(File file);

    boolean contains(String key);

    void set(String key, Object value);

    Object get(String key);
}
