package online.popopo.popopo.console;

import online.popopo.api.function.Variable;
import online.popopo.api.function.command.Command;
import online.popopo.api.function.command.CommandManager;
import online.popopo.api.function.command.SubCommand;
import online.popopo.api.function.listener.ListenerManager;
import online.popopo.api.notice.Notice;
import online.popopo.api.notice.UserNotice.PlayerNotice;
import online.popopo.api.function.Function;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

@Command(name = "console")
public class ConsoleFunc extends Function implements Listener {
    @Variable
    private Plugin plugin;
    @Variable
    private ListenerManager listenerManager;
    @Variable
    private CommandManager commandManager;

    private ProcessHandler handler;

    @Override
    public void enable() {
        handler = new ProcessHandler(plugin);

        listenerManager.register(this);
        commandManager.register(this);
    }

    @SubCommand()
    public void exec(Notice n, String... args) {
        if (!(n instanceof PlayerNotice)) {
            n.bad("Error", "Can't used except player");
        } else if (!SystemUtils.IS_OS_LINUX) {
            n.bad("$", "Can not used except linux");
        } else {
            String text = String.join(" ", args);
            Player p = ((PlayerNotice) n).getPlayer();
            String name = p.getName();

            if (!handler.exec(n, name, text)) {
                n.bad("$", "Already process started");
            }
        }
    }

    @SubCommand(name = "stop")
    public void stop(Notice n) {
        if (!(n instanceof PlayerNotice)) {
            n.bad("Error", "Can't used except player");
        } else {
            Player p = ((PlayerNotice) n).getPlayer();
            String name = p.getName();

            if (!handler.destroy(name)) {
                n.bad("$", "Process was not found");
            }
        }
    }

    @Override
    public void disable() {
        IOUtils.closeQuietly(handler);
    }
}
