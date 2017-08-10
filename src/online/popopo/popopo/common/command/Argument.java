package online.popopo.popopo.common.command;

import online.popopo.popopo.common.message.Caster;

import java.util.Map;

public class Argument {
    private final Caster caster;
    private final Map<String, String> argMap;

    public Argument(Caster c, Map<String, String> a) {
        this.caster = c;
        this.argMap = a;
    }

    public String get(String key) {
        return this.argMap.get(key);
    }

    public Caster respond() {
        return this.caster;
    }
}
