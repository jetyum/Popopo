package online.popopo.api.command;

import online.popopo.api.message.Notice;
import org.apache.commons.lang.ArrayUtils;

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
            int pos = i + e.nameSize();
            Class type = e.argType(i);
            boolean isArray = type.equals(String[].class);
            boolean isFinal = i == o.length - 1;

            if (isArray && isFinal) {
                o[i] = ArrayUtils.subarray(a, pos, a.length);
            } else {
                o[i] = run(type, n, a[pos]);
            }

            if (o[i] == null) {
                return null;
            }
        }

        return o;
    }
}
