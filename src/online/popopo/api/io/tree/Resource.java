package online.popopo.api.io.tree;

import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class Resource extends GzipTree {
    private final Plugin plugin;
    private final String path;

    public Resource(Plugin p, String path) {
        this.plugin = p;
        this.path = path;
    }

    public void load() throws IOException {
        load(plugin.getResource(path));
    }
}
