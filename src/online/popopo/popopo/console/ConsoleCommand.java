package online.popopo.popopo.console;

import online.popopo.api.MainBase;
import online.popopo.api.command.Command;
import online.popopo.api.command.SubCommand;
import online.popopo.api.message.Notice;
import online.popopo.api.message.UserNotice.PlayerNotice;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

@Command(name = "console")
public class ConsoleCommand implements Listener {
    private final ProcessHandler handler;

    public ConsoleCommand(MainBase p) {
        this.handler = new ProcessHandler(p);

        p.registerListener(this);
    }

    @SubCommand()
    public void exec(Notice n, String... args) {
        if (!(n instanceof PlayerNotice)) {
            n.bad("Error", "Can't used except player");

            return;
        } if (!SystemUtils.IS_OS_LINUX) {
            n.bad("$", "Can not used except linux");

            return;
        }

        String text = String.join(" ", args);
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
