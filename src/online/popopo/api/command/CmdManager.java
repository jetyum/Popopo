package online.popopo.api.command;

import online.popopo.api.message.Notice;
import online.popopo.api.message.Formatter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;

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

    public void register(Object o) {
        Class t = o.getClass();
        Annotation a = t.getAnnotation(Command.class);
        String name = ((Command) a).name();
        Handler h = new Handler(o);

        executor(name, (s, cmd, alias, args) -> {
            boolean b = args.length != 0;
            String in = b ? String.join(" ", args) : null;
            Notice n = Notice.create(formatter, s);

            return h.execute(n, in);
        });

        completer(name, (s, cmd, alias, args) -> {
            boolean b = args.length != 0;
            String in = b ? String.join(" ", args) : null;
            Notice n = Notice.create(formatter, s);

            return h.complete(n, in);
        });
    }
}
