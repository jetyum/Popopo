package online.popopo.popopo.voting;

import online.popopo.api.notice.Formatter;
import online.popopo.api.notice.UserNotice.PlayerNotice;
import online.popopo.popopo.voting.Vote.Choice;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Ballot {
    private final Plugin plugin;
    private final PlayerNotice notice;
    private final Vote vote;

    static final int NONE = 0;
    static final int DISPLAYED = 1;
    static final int FINISHED = 2;

    private BukkitRunnable task = null;
    private int index = 0;
    private int state = NONE;

    public Ballot(Plugin p, PlayerNotice n, Vote v) {
        this.plugin = p;
        this.notice = n;
        this.vote = v;
    }

    public int getState() {
        return state;
    }

    public Choice getChoice() {
        return vote.getChoices().get(index);
    }

    public void incrementIndex() {
        index++;

        if (index > vote.getChoices().size() - 1) {
            index -= vote.getChoices().size();
        }
    }

    public void decrementIndex() {
        index--;

        if (index < 0) {
            index += vote.getChoices().size();
        }
    }

    private void show(int fade, String pre, String post) {
        String title = vote.getTitle();
        List<String> list = new ArrayList<>();

        for (Choice c : vote.getChoices()) {
            list.add(c.getName());
        }

        list.set(index, pre + list.get(index) + post);

        String sub = String.join(" / ", list);

        notice.title(title, sub, fade, 10, 10);
    }

    public void open() {
        Formatter f = notice.getFormatter();
        ChatColor c = f.getTheme().getHighlight();
        String pre = "&" + c.getChar();
        String post = "&\\" + c.getChar();

        task = new BukkitRunnable() {
            @Override
            public void run() {
                int fade = state == DISPLAYED ? 0 : 5;

                show(fade, pre, post);
                notice.toast("Please click to vote");
                state = DISPLAYED;
            }
        };

        task.runTaskTimerAsynchronously(plugin, 0, 5);
    }

    public void close(boolean voting) {
        if (state != FINISHED && voting) {
            vote.voteChoice(notice, index);
        }

        if (task != null) {
            task.cancel();
            task = null;
        }

        state = FINISHED;
    }
}
