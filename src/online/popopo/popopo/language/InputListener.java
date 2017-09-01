package online.popopo.popopo.language;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;

public class InputListener implements Listener {
    private static final long MAX_LENGTH = 50;
    private static final long BUFF_SIZE = 10;

    private final JavaPlugin plugin;
    private final Converter converter;
    private final Deque<Set<String>> buffer;
    private final Set<String> roster;

    public InputListener(JavaPlugin p, Converter c) {
        this.plugin = p;
        this.converter = c;
        this.buffer = new LinkedBlockingDeque<>();
        this.roster = new CopyOnWriteArraySet<>();

        Bukkit.getPluginManager().registerEvents(this, p);
    }

    private Set<String> candidateOf(String token) {
        for (Set<String> s : this.buffer) {
            if (s.contains(token)) {
                return s;
            }
        }

        return new HashSet<>();
    }

    @EventHandler
    public void onChatTab(PlayerChatTabCompleteEvent e) {
        Player p = e.getPlayer();
        String token = e.getLastToken();

        if (this.roster.contains(p.getName())) {
            return;
        } else if (token.length() > MAX_LENGTH) {
            return;
        }

        Set<String> c = candidateOf(token);

        if (c.isEmpty()) {
            InputListener o = this;

            c.addAll(this.converter.convert(token, true));
            e.getTabCompletions().clear();

            this.roster.add(p.getName());
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (o.buffer.size() > BUFF_SIZE) {
                        o.buffer.pop();
                    }

                    o.buffer.push(o.converter
                            .convert(token, false));
                    o.roster.remove(p.getName());
                }
            }.runTaskAsynchronously(this.plugin);
        }

        e.getTabCompletions().addAll(c);
    }
}