package online.popopo.common;

import online.popopo.common.command.Command;
import online.popopo.common.command.CmdManager;
import online.popopo.common.config.ConfigIO;
import online.popopo.common.message.Formatter;
import online.popopo.common.message.Theme;
import online.popopo.common.selection.AreaSelector;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PluginBase extends JavaPlugin {
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

    public void register(String name, Command c) {
        command.register(name, c);
    }

    public void register(Listener l) {
        Bukkit.getPluginManager().registerEvents(l, this);
    }
}
