package online.popopo.popopo.console;

import online.popopo.common.command.Command;
import online.popopo.common.command.SubCommand;
import online.popopo.common.message.Caster;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

public class ConsoleCommand implements Command, Listener {
    private final MultiProcess processes;

    public ConsoleCommand(Plugin p) {
        this.processes = new MultiProcess(p);

        Bukkit.getPluginManager().registerEvents(this, p);
    }

    @SubCommand()
    public void exec(Caster c, String text) {
        if (!SystemUtils.IS_OS_LINUX) {
            c.bad("$", "Can not used except linux");

            return;
        }

        String name = c.getTarget().getName();

        if (!processes.exec(c, name, text)) {
            c.bad("$", "Already process started");
        }
    }

    @SubCommand(name = "stop")
    public void stop(Caster c) {
        String name = c.getTarget().getName();

        if (!processes.destroy(name)) {
            c.bad("$", "Process was not found");
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent e) {
        IOUtils.closeQuietly(processes);
    }
}
