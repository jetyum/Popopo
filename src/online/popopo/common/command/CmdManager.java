package online.popopo.common.command;

import online.popopo.common.message.Notice;
import online.popopo.common.message.Formatter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public class CmdManager {
    private final JavaPlugin plugin;
    private final Formatter formatter;

    public CmdManager(JavaPlugin p, Formatter f) {
        this.plugin = p;
        this.formatter = f;
    }

    private void executor(String name, CommandExecutor c) {
        plugin.getCommand(name).setExecutor(c);
    }

    private void completer(String name, TabCompleter c) {
        plugin.getCommand(name).setTabCompleter(c);
    }

    public void register(String name, Command c) {
        Handler h = new Handler(c);

        executor(name, (s, cmd, alias, args) -> {
            String in = String.join(" ", args);
            Notice n = Notice.create(formatter, s);

            return h.execute(n, in);
        });

        completer(name, (s, cmd, alias, args) -> {
            String in = String.join(" ", args);
            Notice n = Notice.create(formatter, s);

            return h.complete(n, in);
        });
    }
}
