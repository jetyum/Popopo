package online.popopo.popopo.console;

import online.popopo.api.command.Command;
import online.popopo.api.command.SubCommand;
import online.popopo.api.notice.Notice;
import online.popopo.api.notice.UserNotice.PlayerNotice;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

@Command(name = "console")
public class ConsoleCommand implements Listener {
    private final ProcessHandler handler;

    public ConsoleCommand(Plugin p) {
        this.handler = new ProcessHandler(p);

        Bukkit.getPluginManager().registerEvents(this, p);
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
