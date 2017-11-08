package online.popopo.api.io.tree;

import com.google.common.io.Files;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Data extends GzipTree {
    private final File file;

    public Data(Plugin p, String path) {
        this.file = new File(p.getDataFolder(), path);
    }

    public void load() throws IOException {
        load(new FileInputStream(file));
    }

    public void save() throws IOException {
        Files.createParentDirs(file);
        save(new FileOutputStream(file));
    }
}
