package online.popopo.common.message;

import org.bukkit.Bukkit;

public class ServerNotice extends Notice {
    ServerNotice(Formatter f) {
        super(f);
    }

    @Override
    public void send(String msg) {
        Bukkit.broadcastMessage(msg);
    }
}
