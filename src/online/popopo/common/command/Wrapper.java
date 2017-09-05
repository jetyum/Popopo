package online.popopo.common.command;

import online.popopo.common.message.Caster;
import online.popopo.common.message.Formatter;
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
    private final Formatter formatter;

    public Wrapper(Definition d, Completer c, Formatter f) {
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
        this.formatter = f;
    }

    public void setTo(JavaPlugin p) {
        p.getCommand(command).setExecutor(this);
    }

    private List<String> inputToArg(String in) {
        List<String> args = new ArrayList<>();
        String[] array = in.split("\"");

        for (int i = 0; i < array.length; i++) {
            if (i % 2 == 1) {
                args.add(array[i]);

                continue;
            }

            for (String s : array[i].split(" ")) {
                if (!s.isEmpty()) {
                    args.add(s);
                }
            }
        }

        return args;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String alias, String[] rawArgs) {
        String in = String.join(" ", rawArgs);
        List<String> args = inputToArg(in);
        Caster m = Caster.newFrom(formatter, s);

        for (SubCommand c : subCommands) {
            if (c.getSize() == args.size()) {
                if (c.matchWith(in)) {
                    Map<String, String> map = new HashMap<>();
                    int start = c.getCommandSize();
                    int argSize = args.size() - start;

                    for (int i = 0; i < argSize; i++) {
                        String key = c.getArgKeys()[i];

                        map.put(key, args.get(i + start));
                    }

                    Argument a = new Argument(s, map);

                    return c.run(object, m, a);
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
        String in = String.join(" ", args);
        Set<String> res = new HashSet<>();
        int end = args.length - 1;

        for (SubCommand c : subCommands) {
            if (c.getSize() >= args.length) {
                if (c.resembleWith(in)) {
                    res.add(c.getCommand().split(" ")[end]);
                } else if (c.matchWith(in)) {
                    int i = end - c.getCommandSize();
                    String k = c.getArgKeys()[i];

                    res.addAll(completer.candidateOf(k));
                }
            }
        }

        res.removeIf(c -> !c.startsWith(args[end]));

        return new ArrayList<>(res);
    }
}