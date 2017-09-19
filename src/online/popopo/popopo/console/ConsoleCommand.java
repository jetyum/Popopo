package online.popopo.popopo.console;

import online.popopo.common.PluginBase;
import online.popopo.common.command.Command;
import online.popopo.common.command.SubCommand;
import online.popopo.common.message.Notice;
import online.popopo.common.message.UserNotice.PlayerNotice;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public class ConsoleCommand implements Command, Listener {
    private final ProcessHandler handler;

    public ConsoleCommand(PluginBase p) {
        this.handler = new ProcessHandler(p);

        p.register(this);
    }

    @SubCommand()
    public void exec(Notice n, String text) {
        if (!(n instanceof PlayerNotice)) {
            n.bad("Error", "Can't used except player");

            return;
        } if (!SystemUtils.IS_OS_LINUX) {
            n.bad("$", "Can not used except linux");

            return;
        }

        Player p = ((PlayerNotice) n).getPlayer();
        String name = p.getName();

        if (!handler.exec(n, name, text)) {
            n.bad("$", "Already process started");
        }
    }

    @SubCommand(name = "stop")
    public void stop(Notice n) {
        if (!(n instanceof PlayerNotice)) {
            n.bad("Error", "Can't used except player");

            return;
        }

        Player p = ((PlayerNotice) n).getPlayer();
        String name = p.getName();

        if (!handler.destroy(name)) {
            n.bad("$", "Process was not found");
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent e) {
        IOUtils.closeQuietly(handler);
    }
}
