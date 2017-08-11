package online.popopo.popopo.language;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class InputListener implements Listener {
    private static final long MAX_LENGTH = 40;
    private static final long BUFF_SIZE = 10;

    private final JavaPlugin plugin;
    private final Converter converter;
    private final Deque<Set<String>> buffer;

    public InputListener(JavaPlugin p, Converter c) {
        this.plugin = p;
        this.converter = c;
        this.buffer = new ArrayDeque<>();

        Bukkit.getPluginManager().registerEvents(this, p);

    }

    public Set<String> candidateOf(String token) {
        for (Set<String> s : this.buffer) {
            if (s.contains(token)) {
                return s;
            }
        }

        return null;
    }

    @EventHandler
    public void onChatTab(PlayerChatTabCompleteEvent e) {
        Player p = e.getPlayer();
        String token = e.getLastToken();

        if (token.length() > MAX_LENGTH) {
            return;
        }

        Set<String> set = candidateOf(token);

        if (set != null) {
            e.getTabCompletions().addAll(set);

            return;
        }

        InputListener o = this;

        e.getTabCompletions().clear();

        new BukkitRunnable() {
            @Override
            public void run() {
                Set<String> s = o.converter.convert(token);

                if (o.buffer.size() > BUFF_SIZE) {
                    o.buffer.pop();
                }

                o.buffer.push(s);
            }
        }.runTask(this.plugin);
    }
}