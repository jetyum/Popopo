package online.popopo.api;

import online.popopo.api.command.Command;
import online.popopo.api.command.CmdManager;
import online.popopo.api.io.ConfigIO;
import online.popopo.api.message.Formatter;
import online.popopo.api.message.Theme;
import online.popopo.api.selection.AreaSelector;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class MainBase extends JavaPlugin {
    private ConfigIO io = new ConfigIO(this);
    private Theme theme = new Theme();
    private Formatter formatter = new Formatter(theme);
    private CmdManager command;
    private AreaSelector selector;

    public ConfigIO getIO() {
        return io;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public AreaSelector getSelector() {
        return selector;
    }

    @Override
    public void onLoad() {
        command = new CmdManager(this, formatter);
    }

    @Override
    public void onEnable() {
        selector = new AreaSelector(this, formatter);
    }

    public void loadTheme(String path) {
        io.read(path, theme);
    }

    public void register(Object o) {
        command.register(o);
    }

    public void register(Listener l) {
        Bukkit.getPluginManager().registerEvents(l, this);
    }
}
