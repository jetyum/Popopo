package online.popopo.popopo.common.command;

import org.bukkit.command.CommandSender;

import java.util.Map;

public class InputProperty {
    private final CommandSender sender;
    private final Map<String, String> argMap;

    public InputProperty(CommandSender s, Map<String, String> a) {
        this.sender = s;
        this.argMap = a;
    }

    public String get(String key) {
        return argMap.get(key);
    }

    public CommandSender getSender() {
        return
    }
}
