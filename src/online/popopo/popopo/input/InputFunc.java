package online.popopo.popopo.input;

import online.popopo.api.function.Variable;
import online.popopo.api.function.listener.ListenerManager;
import online.popopo.api.io.Injector;
import online.popopo.api.io.tree.Resource;
import online.popopo.api.notice.Formatter;
import online.popopo.api.function.Function;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;

public class InputFunc extends Function implements Listener {
    private static final long MAX_LENGTH = 50;
    private static final long BUFF_SIZE = 10;

    @Variable
    private Plugin plugin;
    @Variable
    private Formatter formatter;
    @Variable
    private ListenerManager listenerManager;

    private final Japanese japanese;
    private final Deque<Set<String>> buffer;
    private final Set<String> roster;

    public InputFunc() {
        this.japanese = new Japanese();
        this.buffer = new LinkedBlockingDeque<>();
        this.roster = new CopyOnWriteArraySet<>();
    }

    @Override
    public void load() {
        try {
            Resource r = new Resource(plugin, "kana.gz");
            r.load();
            Injector.inject(r, japanese);
        } catch (IOException e) {
            plugin.getLogger().info("Language wasn't loaded");
        }
    }

    @Override
    public void enable() {
        listenerManager.register(this);
    }

    private Set<String> candidateOf(String token) {
        for (Set<String> s : buffer) {
            if (s.contains(token)) return s;
        }

        return new HashSet<>();
    }

    @EventHandler
    public void onChatTab(PlayerChatTabCompleteEvent e) {
        Player p = e.getPlayer();
        String s = e.getLastToken();

        if (roster.contains(p.getName())) {
            return;
        } else if (s.length() > MAX_LENGTH) {
            return;
        }

        Collection<String> c = e.getTabCompletions();

        c.clear();
        c.addAll(candidateOf(s));

        if (!c.isEmpty()) return;

        c.addAll(japanese.convert(s, true));

        roster.add(p.getName());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (buffer.size() > BUFF_SIZE) {
                    buffer.pop();
                }

                buffer.push(japanese.convert(s, false));
                roster.remove(p.getName());
            }
        }.runTaskAsynchronously(plugin);
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