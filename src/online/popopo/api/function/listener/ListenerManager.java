package online.popopo.api.function.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ListenerManager {
    private final Plugin plugin;

    public ListenerManager(Plugin p) {
        this.plugin = p;
    }

    public void register(Listener l) {
        Bukkit.getPluginManager()
                .registerEvents(l, plugin);
    }

    public void register(Listener... l) {
        for (Listener listener : l) register(listener);
    }
}
