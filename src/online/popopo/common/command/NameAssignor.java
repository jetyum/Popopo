package online.popopo.common.command;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class NameAssignor extends HashMap<Class, Method> {
    private final Command command;

    NameAssignor(Command c) {
        this.command = c;
    }

    private Collection<String> run(Class t) {
        try {
            Set<String> set = new HashSet<>();

            if (containsKey(t)) {
                Object o = get(t).invoke(command);

                return set.getClass().cast(o);
            } else {
                return set;
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    Set<String> get(Executor e, int i, String[] a) {
        Set<String> set = new HashSet<>();

        if (i < e.nameSize()) {
            set.add(e.name(i));
        } else {
            set.addAll(run(e.argType(i - e.nameSize())));
        }

        set.removeIf(s -> !s.startsWith(a[i]));

        return set;
    }
}
