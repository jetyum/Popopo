package online.popopo.common.command;

import online.popopo.common.message.Caster;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

class ValueAssignor extends HashMap<Class, Method> {
    private final Command command;

    ValueAssignor(Command c) {
        this.command = c;
    }

    private Object run(Class t, Caster c, String arg) {
        try {
            if (containsKey(t)) {
                return get(t).invoke(command, c, arg);
            } else {
                return arg;
            }
        } catch (InvocationTargetException
                | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    Object[] get(Executor e, Caster c, String[] a) {
        Object[] o = new Object[e.argsSize()];

        for (int i = 0; i < o.length; i++) {
            o[i] = run(e.argType(i), c, a[i + e.nameSize()]);

            if (o[i] == null) {
                return null;
            }
        }

        return o;
    }
}
