package online.popopo.common.command;

import online.popopo.common.message.Caster;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class SubCommand implements Comparable<SubCommand> {
    private final String command;
    private final Method method;
    private final String[] argKeys;
    private final int size;
    private final int commandSize;

    public SubCommand(Method m) {
        Executor e = m.getAnnotation(Executor.class);
        String[] v = e.value();
        String s = String.join(" ", v).trim();

        this.command = v[0];
        this.method = m;
        this.argKeys = Arrays.copyOfRange(v, 1, v.length);
        this.size = s.isEmpty() ? 0 : s.split(" ").length;
        this.commandSize = this.size - v.length + 1;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgKeys() {
        return argKeys;
    }

    public int getSize() {
        return size;
    }

    public int getCommandSize() {
        return commandSize;
    }

    public boolean matchWith(String s) {
        return command.isEmpty()
                || (s + " ").startsWith(command + " ");
    }

    public boolean resembleWith(String s) {
        return !command.isEmpty()
                && command.startsWith(s);
    }

    public boolean run(Object o, Caster c, Argument a){
        try {
            method.invoke(o, c, a);

            return true;
        } catch (InvocationTargetException
                | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int compareTo(SubCommand c) {
        return size - c.size;
    }
}