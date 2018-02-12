package online.popopo.popopo;

import online.popopo.api.command.CmdManager;
import online.popopo.api.io.*;
import online.popopo.api.io.tree.Config;
import online.popopo.api.io.tree.Data;
import online.popopo.api.io.tree.Resource;
import online.popopo.api.notice.Formatter;
import online.popopo.api.notice.Theme;
import online.popopo.api.selection.AreaSelector;
import online.popopo.popopo.input.InputListener;
import online.popopo.popopo.console.ConsoleCommand;
import online.popopo.popopo.news.NewsListener;
import online.popopo.popopo.news.ServerNews;
import online.popopo.popopo.portal.Portal;
import online.popopo.popopo.portal.PortalCommand;
import online.popopo.popopo.portal.PortalListener;
import online.popopo.popopo.protection.Judge;
import online.popopo.popopo.protection.License;
import online.popopo.popopo.protection.ProtectCommand;
import online.popopo.popopo.protection.Reserve;
import online.popopo.popopo.protection.listener.*;
import online.popopo.popopo.selection.SelectCommand;
import online.popopo.popopo.system.SystemCommand;
import online.popopo.popopo.input.Japanese;
import online.popopo.popopo.domain.TeleportListener;
import online.popopo.popopo.voting.VoteCommand;
import online.popopo.popopo.world.TransferCommand;
import online.popopo.popopo.world.WorldInfo;
import online.popopo.popopo.world.WorldLoader;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main extends JavaPlugin {
    @Inject(key = "portals")
    private Map<String, Portal> portals = new HashMap<>();
    @Inject(key = "reserves")
    private Map<String, Reserve> reserves = new HashMap<>();

    private Theme theme = new Theme();
    private Formatter formatter = new Formatter(theme);
    private CmdManager command;
    private AreaSelector selector;
    private Japanese jpn = new Japanese();
    private ServerNews news = new ServerNews();
    private Map<String, WorldInfo> worlds = new HashMap<>();
    private Map<String, License> licenses = new HashMap<>();

    public File getDataFile(String path) {
        return new File(getDataFolder(), path);
    }

    public void load() {
        try {
            Config c = new Config(this, "theme.yml");
            c.load();
            Injector.inject(c, theme);
        } catch (IOException e) {
            getLogger().info("Theme wasn't loaded");
        }

        try {
            Data d = new Data(this, ".data/data.gz");
            d.load();
            Injector.inject(d, this);
        } catch (IOException e) {
            getLogger().info("Data wasn't loaded");
        }

        try {
            Resource r = new Resource(this, "kana.gz");
            r.load();
            Injector.inject(r, jpn);
        } catch (IOException e) {
            getLogger().info("Language wasn't loaded");
        }

        try {
            Config c = new Config(this, "news.yml");
            c.load();
            Injector.inject(c, news);
        } catch (IOException e) {
            getLogger().info("Server news wasn't loaded");
        }

        try {
            Config c = new Config(this, "world.yml");
            c.load();
            Injector.inject(c, worlds, WorldInfo.class);
        } catch (IOException e) {
            getLogger().info("World wasn't loaded");
        }

        try {
            Config c = new Config(this, "license.yml");
            c.load();
            Injector.inject(c, licenses, License.class);
        } catch (IOException e) {
            getLogger().info("License wasn't loaded");
        }
    }

    @Override
    public void onLoad() {
        command = new CmdManager(this, formatter);

        load();
    }

    public void registerListeners(Listener... l) {
        for (Listener listener : l) {
            Bukkit.getPluginManager()
                    .registerEvents(listener, this);
        }
    }

    public void registerCommands(Object... o) {
        for (Object object : o) {
            command.register(object);
        }
    }

    @Override
    public void onEnable() {
        selector = new AreaSelector(this, formatter);

        WorldLoader.load(this, worlds);

        Judge judge = new Judge(reserves, licenses);

        registerListeners(
                new InputListener(this, jpn, formatter),
                new TeleportListener(this),
                new PortalListener(portals),
                new BlockListener(judge),
                new EntityListener(judge),
                new ExplosionListener(judge),
                new PlayerListener(judge),
                new NewsListener(this, news, formatter)
        );

        registerCommands(
                new SelectCommand(selector),
                new SystemCommand(),
                new ConsoleCommand(this),
                new TransferCommand(),
                new PortalCommand(selector, portals),
                new ProtectCommand(selector, judge),
                new VoteCommand(this, formatter)
        );
    }

    @Override
    public void onDisable() {
        try {
            Data d = new Data(this, ".data/data.gz");
            Injector.inject(this, d);
            d.save();
        } catch (IOException e) {
            getLogger().info("Data wasn't saved");
        }
    }
}
