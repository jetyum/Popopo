package online.popopo.common.command;

import org.bukkit.command.CommandSender;

import java.util.Map;

public class Argument {
    private final CommandSender sender;
    private final Map<String, String> argMap;

    public Argument(CommandSender s, Map<String, String> a) {
        this.sender = s;
        this.argMap = a;
    }

    public String get(String key) {
        return this.argMap.get(key);
    }
}
