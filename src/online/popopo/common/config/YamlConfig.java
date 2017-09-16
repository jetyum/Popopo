package online.popopo.common.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class YamlConfig implements Config {
    private final FileConfiguration config;

    public YamlConfig() {
        this.config = new YamlConfiguration();
    }

    @Override
    public void load(InputStream in) {
        try {
            config.load(new InputStreamReader(in));
        } catch (InvalidConfigurationException
                | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean contains(String key) {
        return config.contains(key);
    }

    @Override
    public void set(String key, Object value) {
        config.set(key, value);
    }

    @Override
    public Object get(String key) {
        return config.get(key);
    }
}
