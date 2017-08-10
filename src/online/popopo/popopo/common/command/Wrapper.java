package online.popopo.popopo.common.command;

import online.popopo.popopo.common.message.Caster;
import online.popopo.popopo.common.message.PluginCaster;
import online.popopo.popopo.common.message.Theme;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.*;

public class Wrapper implements TabExecutor {
    private final Definition object;
    private final String command;
    private final List<SubCommand> subCommands;
    private final Completer completer;
    private final Theme theme;

    public Wrapper(Definition d, Completer c, Theme t) {
        List<SubCommand> subCommands = new ArrayList<>();

        for (Method m : d.getClass().getMethods()) {
            if (m.isAnnotationPresent(Executor.class)) {
                subCommands.add(new SubCommand(m));
            }
        }

        Collections.sort(subCommands);
        Collections.reverse(subCommands);

        this.object = d;
        this.command = d.getCommand();
        this.subCommands = subCommands;
        this.completer = c;
        this.theme = t;
    }

    public void setTo(JavaPlugin p) {
        p.getCommand(this.command).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        String in = String.join(" ", args);
        SubCommand s = null;

        for (SubCommand c : this.subCommands) {
            if (c.getSize() == args.length) {
                if (c.matchWith(in)) {
                    s = c;
                }
            }
        }

        Caster c = PluginCaster.newFrom(this.theme, sender);

        return s != null && s.run(this.object, c, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        String in = String.join(" ", args);
        Set<String> res = new HashSet<>();
        int end = args.length - 1;

        for (SubCommand c : this.subCommands) {
            if (c.getSize() >= args.length) {
                if (c.resembleWith(in)) {
                    res.add(c.getCommand().split(" ")[end]);
                } else if (c.matchWith(in)) {
                    int i = end - c.getCommandSize();
                    String k = c.getArgKeys()[i];

                    res.addAll(this.completer.candidateOf(k));
                }
            }
        }

        res.removeIf(s -> !s.startsWith(args[end]));

        return new ArrayList<>(res);
    }
}