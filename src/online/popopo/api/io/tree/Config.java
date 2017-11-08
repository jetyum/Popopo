package online.popopo.api.io.tree;

import com.google.common.io.Files;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class Config extends YamlTree {
    private final File file;

    public Config(Plugin p, String path) {
        this.file = new File(p.getDataFolder(), path);
    }

    public void load() throws IOException {
        load(file);
    }

    public void save() throws IOException {
        Files.createParentDirs(file);
        save(file);
    }
}
