package online.popopo.popopo;

import online.popopo.api.function.FunctionManager;
import online.popopo.api.function.command.CommandManager;
import online.popopo.api.io.*;
import online.popopo.api.io.tree.Config;
import online.popopo.api.notice.Formatter;
import online.popopo.api.notice.Theme;
import online.popopo.api.selection.AreaSelector;
import online.popopo.api.function.listener.ListenerManager;
import online.popopo.popopo.domain.DomainFunc;
import online.popopo.popopo.input.InputFunc;
import online.popopo.popopo.notice.NoticeFunc;
import online.popopo.popopo.portal.PortalFunc;
import online.popopo.popopo.protection.ProtectFunc;
import online.popopo.popopo.selection.SelectFunc;
import online.popopo.popopo.system.SystemFunc;
import online.popopo.popopo.voting.VoteFunc;
import online.popopo.popopo.transfer.TransferFunc;
import online.popopo.popopo.world.WorldFunc;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Main extends JavaPlugin {
    private final Theme theme = new Theme();
    private final Formatter formatter = new Formatter(theme);
    private final FunctionManager manager = new FunctionManager();

    @Override
    public void onLoad() {
        try {
            Config c = new Config(this, "theme.yml");
            c.load();
            Injector.inject(c, theme);
        } catch (IOException e) {
            getLogger().info("Theme wasn't loaded");
        }

        manager.register(
                new WorldFunc(),
                new VoteFunc(),
                new TransferFunc(),
                new SystemFunc(),
                new SelectFunc(),
                new ProtectFunc(),
                new PortalFunc(),
                new NoticeFunc(),
                new InputFunc(),
                new DomainFunc()
        );
        manager.registerVariable(
                this,
                formatter,
                new CommandManager(this, formatter),
                new ListenerManager(this)
        );
        manager.loadFunctions();
    }

    @Override
    public void onEnable() {
        manager.registerVariable(
                new AreaSelector(this, formatter)
        );
        manager.enableFunctions();
    }

    @Override
    public void onDisable() {
        manager.disableFunctions();
    }
}
