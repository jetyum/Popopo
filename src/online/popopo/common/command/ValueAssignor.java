package online.popopo.common.command;

import online.popopo.common.message.Notice;

import java.lang.reflect.Method;
import java.util.HashMap;

class ValueAssignor extends HashMap<Class, Method> {
    private final Command command;

    ValueAssignor(Command c) {
        this.command = c;
    }

    private Object run(Class t, Notice n, String arg) {
        try {
            if (containsKey(t)) {
                return get(t).invoke(command, n, arg);
            } else {
                return arg;
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    Object[] get(Executor e, Notice n, String[] a) {
        Object[] o = new Object[e.argsSize()];

        for (int i = 0; i < o.length; i++) {
            String s = a[i + e.nameSize()];

            o[i] = run(e.argType(i), n, s);

            if (o[i] == null) {
                return null;
            }
        }

        return o;
    }
}
