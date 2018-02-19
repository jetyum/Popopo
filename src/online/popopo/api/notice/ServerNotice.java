package online.popopo.api.notice;

import org.bukkit.Bukkit;

public class ServerNotice extends Notice {
    ServerNotice(Formatter f) {
        super(f);
    }

    @Override
    public void send(String msg) {
        Bukkit.getOnlinePlayers().forEach((p) -> {
            p.sendMessage(msg);
        });
    }
}
