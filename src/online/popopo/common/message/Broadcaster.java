package online.popopo.common.message;

import org.bukkit.Bukkit;

public class Broadcaster extends Castable {
    public Broadcaster(Formatter f) {
        super(f);
    }

    @Override
    public void cast(String msg) {
        Bukkit.broadcastMessage(msg);
    }
}
