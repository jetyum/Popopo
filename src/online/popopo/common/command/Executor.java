package online.popopo.common.command;

import online.popopo.common.message.Notice;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;

class Executor {
    private final Command command;
    private final Method method;
    private final String[] name;
    private final String fullname;
    private final Class[] argTypes;
    private final int size;

    Executor(Command c, Method m, String[] name) {
        this.command = c;
        this.method = m;
        this.name = name;
        this.fullname = String.join(" ", name);

        int argsSize = m.getParameters().length - 1;
        Class[] a = m.getParameterTypes();
        Class[] argTypes = new Class[argsSize];

        System.arraycopy(a, 1, argTypes, 0, argsSize);

        this.argTypes = argTypes;
        this.size = name.length + argsSize;
    }

    String name(int index) {
        return name[index];
    }

    Class argType(int index) {
        return argTypes[index];
    }

    int nameSize() {
        return name.length;
    }

    int argsSize() {
        return argTypes.length;
    }

    boolean matches(String in, int size) {
        if (size < this.size) {
            return false;
        } else if (fullname.isEmpty()) {
            return true;
        }

        return (in + " ").startsWith(fullname + " ");
    }

    boolean contains(String in, int size) {
        if (size > this.size) {
            return false;
        } else if (fullname.isEmpty()) {
            return true;
        }

        return (" " + fullname).startsWith(" " + in)
                || (in + " ").startsWith(fullname + " ");
    }

    void run(Notice n, Object[] o) {
        try {
            Object[] args = new Object[] {n};

            args = ArrayUtils.addAll(args, o);
            method.invoke(command, args);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
