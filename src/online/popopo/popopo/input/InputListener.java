package online.popopo.popopo.input;

import online.popopo.common.message.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
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
    private final Formatter formatter;
    private final Deque<Set<String>> buffer;
    private final Set<String> roster;

    public InputListener(JavaPlugin p, Converter c, Formatter f) {
        this.plugin = p;
        this.converter = c;
        this.formatter = f;
        this.buffer = new LinkedBlockingDeque<>();
        this.roster = new CopyOnWriteArraySet<>();

        Bukkit.getPluginManager().registerEvents(this, p);
    }

    private Set<String> candidateOf(String token) {
        for (Set<String> s : buffer) {
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

        if (roster.contains(p.getName())) {
            return;
        } else if (token.length() > MAX_LENGTH) {
            return;
        }

        Set<String> c = candidateOf(token);

        if (c.isEmpty()) {
            c.addAll(converter.convert(token, true));
            e.getTabCompletions().clear();

            roster.add(p.getName());
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (buffer.size() > BUFF_SIZE) {
                        buffer.pop();
                    }

                    buffer.push(converter
                            .convert(token, false));
                    roster.remove(p.getName());
                }
            }.runTaskAsynchronously(plugin);
        }

        e.getTabCompletions().addAll(c);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setMessage(formatter.standard(e.getMessage()));
    }

    @EventHandler
    public void onSighChange(SignChangeEvent e) {
        String[] lines = e.getLines();

        for (int i = 0; i < lines.length; i++) {
            lines[i] = formatter.standard(lines[i]);
        }
    }
}