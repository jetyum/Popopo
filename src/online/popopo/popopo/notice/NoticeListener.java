package online.popopo.popopo.notice;

import online.popopo.api.notice.Formatter;
import online.popopo.api.notice.Guideable;
import online.popopo.api.notice.Notice;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NoticeListener implements Listener, Runnable {
    private final Plugin plugin;
    private final ServerNotice news;
    private final Formatter formatter;
    private final Random random;

    public NoticeListener(Plugin p, ServerNotice n, Formatter f) {
        this.plugin = p;
        this.news = n;
        this.formatter = f;
        this.random = new Random();

        if (n.getInfoArticles() != null) {
            BukkitScheduler s = Bukkit.getScheduler();
            int t = n.getInfoPeriod();

            s.runTaskTimerAsynchronously(p, this, t, t);
        }
    }

    public void showArticle(Notice n, String prefix,
                            Map<String, String> article) {
        if (!article.containsKey("title")) {
            n.bad("Error", "There wasn't title");

            return;
        }

        n.guide(prefix, new Guideable() {
            @Override
            public String getLoreTitle() {
                return article.get("title");
            }

            @Override
            public List<String> getLore() {
                String s = article.getOrDefault("body", "");

                return Collections.singletonList(s);
            }
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Notice n = Notice.create(formatter, p);
        List<Map<String, String>> a = news.getNewsArticles();
        BukkitScheduler s = Bukkit.getScheduler();

        s.runTaskAsynchronously(plugin, () -> {
            if (a == null) return;

            int size = a.size();

            if (size > news.getNewsNumber()) {
                size = news.getNewsNumber();
            }

            for (int i = 0; i < size; i++) {
                showArticle(n, "News", a.get(i));
            }
        });
    }

    @Override
    public void run() {
        Notice n = Notice.create(formatter);
        List<Map<String, String>> a = news.getInfoArticles();
        int i = random.nextInt(a.size());

        showArticle(n, "Info", a.get(i));
    }
}
