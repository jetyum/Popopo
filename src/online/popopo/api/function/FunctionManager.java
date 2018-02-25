package online.popopo.api.function;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FunctionManager {
    private final List<Function> functions;

    public FunctionManager() {
        this.functions = new ArrayList<>();
    }

    public void register(Function f) {
        functions.add(f);
    }

    public void register(Function... f) {
        for (Function function : f) {
            register(function);
        }
    }

    public void registerVariable(Object o) {
        for (Function function : functions) {
            Class t = function.getClass();

            for (Field f : t.getDeclaredFields()) {
                if (f.isAnnotationPresent(Variable.class)
                        && f.getType().isInstance(o)) try {
                    boolean flag = f.isAccessible();

                    f.setAccessible(true);
                    f.set(function, o);
                    f.setAccessible(flag);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void registerVariable(Object... o) {
        for (Object object : o) {
            registerVariable(object);
        }
    }

    public void loadFunctions() {
        functions.forEach(Function::load);
    }

    public void enableFunctions() {
        functions.forEach(Function::enable);
    }

    public void disableFunctions() {
        functions.forEach(Function::disable);
    }
}
