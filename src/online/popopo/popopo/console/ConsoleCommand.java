package online.popopo.popopo.console;

import online.popopo.common.PluginBase;
import online.popopo.common.command.Command;
import online.popopo.common.command.SubCommand;
import online.popopo.common.message.Caster;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
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
    public void exec(Caster c, String text) {
        if (!SystemUtils.IS_OS_LINUX) {
            c.bad("$", "Can not used except linux");

            return;
        }

        String name = c.getTarget().getName();

        if (!handler.exec(c, name, text)) {
            c.bad("$", "Already process started");
        }
    }

    @SubCommand(name = "stop")
    public void stop(Caster c) {
        String name = c.getTarget().getName();

        if (!handler.destroy(name)) {
            c.bad("$", "Process was not found");
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent e) {
        IOUtils.closeQuietly(handler);
    }
}
