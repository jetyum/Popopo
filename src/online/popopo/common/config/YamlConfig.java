package online.popopo.common.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class YamlConfig extends Config {
    private final String path;
    private final FileConfiguration config;
    private final Plugin plugin;

    public YamlConfig(String path) {
        this.path = path;
        this.config = new YamlConfiguration();
        this.plugin = null;
    }

    private YamlConfig(FileConfiguration c, Plugin p) {
        this.path = "";
        this.config = c;
        this.plugin = p;
    }

    public static YamlConfig newFrom(Plugin p) {
        p.saveDefaultConfig();

        return new YamlConfig(p.getConfig(), p);
    }

    public static YamlConfig newFrom(Plugin p, String path) throws IOException {
        String dir = p.getDataFolder().getAbsolutePath();
        String abs = dir + "/" + path;

        if (!new File(abs).exists()) {
            p.saveResource(path, false);
        }

        return new YamlConfig(abs);
    }

    @Override
    public boolean load() {
        if (plugin != null) {
            plugin.reloadConfig();

            return true;
        }

        try {
            config.load(path);

            return true;
        } catch (InvalidConfigurationException
                | IOException e) {
            return false;
        }
    }

    @Override
    public boolean save() {
        if (plugin != null) {
            plugin.saveConfig();

            return true;
        }

        try {
            config.save(path);

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean contain(String section, String key) {
        return config.isSet(section + "." + key);
    }

    @Override
    public void set(String section, String key, Object value) {
        config.set(section + "." + key, value);
    }

    @Override
    public Object get(String section, String key) {
        return config.get(section + "." + key);
    }
}
