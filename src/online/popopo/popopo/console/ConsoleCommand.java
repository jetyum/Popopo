package online.popopo.popopo.console;

import online.popopo.popopo.common.command.Argument;
import online.popopo.popopo.common.command.Definition;
import online.popopo.popopo.common.command.Executor;
import online.popopo.popopo.common.message.Caster;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ConsoleCommand implements Definition, Listener {
    private final JavaPlugin plugin;
    private final MultiProcess processes;

    public ConsoleCommand(JavaPlugin p) {
        this.plugin = p;
        this.processes = new MultiProcess(p);

        Bukkit.getPluginManager().registerEvents(this, p);
    }

    @Override
    public String getCommand() {
        return "console";
    }

    @Executor({"", "text"})
    public void onCommand(Caster c, Argument arg) {
        String text = arg.get("text");
        String name = arg.getSender().getName();

        if (!this.processes.exec(c, name, text)) {
            c.bad("$", "Already process started");
        }
    }

    @Executor({"destroy"})
    public void onCloseCommand(Caster c, Argument arg) {
        String name = arg.getSender().getName();

        if (!this.processes.destroy(name)) {
            c.bad("$", "Process was not found");
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent e) {
        IOUtils.closeQuietly(this.processes);
    }
}
