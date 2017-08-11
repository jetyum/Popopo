package online.popopo.popopo.language;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
    private static final long MAX_LENGTH = 40;
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

        return null;
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

        Set<String> set = candidateOf(token);

        if (set != null) {
            e.getTabCompletions().addAll(set);

            return;
        }

        e.getTabCompletions().clear();
        this.roster.add(p.getName());
        if (this.buffer.size() > BUFF_SIZE) {
            this.buffer.pop();
        }

        InputListener o = this;

        new BukkitRunnable() {
            @Override
            public void run() {
                o.buffer.push(o.converter.convert(token));
                o.roster.remove(p.getName());

                p.playSound(p.getLocation(),
                        Sound.ENTITY_CHICKEN_EGG, 1.0f, 0.9f);
            }
        }.runTaskAsynchronously(this.plugin);
    }
}