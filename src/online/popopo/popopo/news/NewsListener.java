package online.popopo.popopo.news;

import online.popopo.api.notice.Formatter;
import online.popopo.api.notice.Notice;
import online.popopo.popopo.news.ServerNews.Article;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

public class NewsListener implements Listener {
    private final Plugin plugin;
    private final ServerNews news;
    private final Formatter formatter;

    public NewsListener(Plugin p, ServerNews s, Formatter f) {
        this.plugin = p;
        this.news = s;
        this.formatter = f;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Notice n = Notice.create(formatter, p);
        List<Article> articles = news.getArticles();
        BukkitScheduler s = Bukkit.getScheduler();

        s.runTask(plugin, () -> {
            if (articles.isEmpty()) {
                n.info("Info", "Article doesn't exist");
            } else {
                int size = articles.size();

                if (size > news.getNumber()) {
                    size = news.getNumber();
                }

                n.info("Info", "Show articles of news");

                for (int i = 0; i < size; i++) {
                    n.guide("News", articles.get(i));
                }
            }
        });
    }
}
