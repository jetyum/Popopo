package online.popopo.common.message;

import org.bukkit.Bukkit;

public class Broadcaster extends Castable {
    public Broadcaster(Theme t) {
        super(t);
    }

    @Override
    public void cast(String msg) {
        Bukkit.broadcastMessage(msg);
    }
}
