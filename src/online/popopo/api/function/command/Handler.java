package online.popopo.api.function.command;

import online.popopo.api.notice.Notice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Handler {
    private static final Pattern pattern
            = Pattern.compile("(^|(?<=\\s))" +
            "(\"?)(.*?)\\2($|(?=\\s))");

    private final List<Executor> executors;
    private final NameAssignor nameAssignor;
    private final ValueAssignor valueAssignor;

    Handler(Object o) {
        this.executors = new ArrayList<>();
        this.nameAssignor = new NameAssignor(o);
        this.valueAssignor = new ValueAssignor(o);

        for (Method m : o.getClass().getMethods()) {
            for (Annotation a : m.getAnnotations()) {
                if (a instanceof SubCommand) {
                    String[] k = ((SubCommand) a).name();
                    Executor v = new Executor(o, m, k);

                    this.executors.add(v);
                } else if (a instanceof NameGetter) {
                    Class k = ((NameGetter) a).type();

                    this.nameAssignor.put(k, m);
                } else if (a instanceof ValueGetter) {
                    Class k = ((ValueGetter) a).type();

                    this.valueAssignor.put(k, m);
                }
            }
        }

        this.executors.sort(
                (a, b) -> b.nameSize() - a.nameSize());
    }

    private String[] getArgsFrom(String in) {
        List<String> keys = new ArrayList<>();

        if (in != null) {
            Matcher m = pattern.matcher(in);

            while (m.find()) keys.add(m.group(3));
        }

        return keys.toArray(new String[0]);
    }

    boolean execute(Notice n, String in) {
        String[] args = getArgsFrom(in);

        for (Executor e : executors) {
            if (!e.matches(in, args.length)) {
                continue;
            }

            Object[] o = valueAssignor.get(e, n, args);

            if (o != null) e.run(n, o);

            return true;
        }

        return false;
    }

    List<String> complete(Notice n, String in) {
        String[] args = getArgsFrom(in);
        List<String> list = new ArrayList<>();

        for (Executor e : executors) {
            if (!e.contains(in, args.length)) {
                continue;
            }

            int end = args.length - 1;

            list.addAll(nameAssignor.get(e, end, args));
        }

        return list;
    }
}
