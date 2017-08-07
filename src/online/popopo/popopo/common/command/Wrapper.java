package online.popopo.popopo.common.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.*;

public class Wrapper implements TabExecutor {
    private final Definition definition;
    private final String command;
    private final List<SubCommand> subCommands;
    private final Completer completer;

    public Wrapper(Definition def, Completer comp) {
        List<SubCommand> subCommands = new ArrayList<>();

        for (Method m : def.getClass().getMethods()) {
            if (m.isAnnotationPresent(Executor.class)) {
                subCommands.add(new SubCommand(m));
            }
        }

        Collections.sort(subCommands);
        Collections.reverse(subCommands);

        this.definition = def;
        this.command = def.getCommand();
        this.subCommands = subCommands;
        this.completer = comp;
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

        if (s == null) {
            return false;
        }

        try {
            Map<String, String> arg = new HashMap<>();
            int start = s.getCommandSize();
            int top = args.length - s.getCommandSize();

            for (int i = 0; i < top; i++) {
                arg.put(s.getArgKeys()[i], args[i + start]);
            }

            return (Boolean) s.getMethod()
                    .invoke(this.definition, arg);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        String in = String.join(" ", args);
        Set<String> res = new HashSet<>();
        int end = args.length - 1;

        for (SubCommand c : this.subCommands) {
            int top = end - c.getCommandSize();

            if (c.getSize() >= args.length) {
                if (c.resembleWith(in)) {
                    res.add(c.getCommand().split(" ")[end]);
                } else if (c.matchWith(in)) {
                    String key = c.getArgKeys()[top];
                    res.addAll(this.completer.candidateOf(key));
                }
            }

            res.removeIf(s -> !s.startsWith(args[end]));
        }

        return new ArrayList<>(res);
    }
}