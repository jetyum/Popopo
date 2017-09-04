package online.popopo.popopo.console;

import online.popopo.common.command.Argument;
import online.popopo.common.command.Definition;
import online.popopo.common.command.Executor;
import online.popopo.common.message.Caster;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ConsoleCommand implements Definition, Listener {
    private final MultiProcess processes;

    public ConsoleCommand(JavaPlugin p) {
        this.processes = new MultiProcess(p);

        Bukkit.getPluginManager().registerEvents(this, p);
    }

    @Executor({"", "text"})
    public void onCommand(Caster c, Argument arg) {
        if (!SystemUtils.IS_OS_LINUX) {
            c.bad("$", "Can not used except linux");

            return;
        }

        String text = arg.get("text");
        String name = c.getTarget().getName();

        if (!processes.exec(c, name, text)) {
            c.bad("$", "Already process started");
        }
    }

    @Executor({"destroy"})
    public void onDestroyCommand(Caster c, Argument arg) {
        String name = c.getTarget().getName();

        if (!processes.destroy(name)) {
            c.bad("$", "Process was not found");
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent e) {
        IOUtils.closeQuietly(processes);
    }

    @Override
    public String getCommand() {
        return "console";
    }
}
