package online.popopo.popopo.voting;

import online.popopo.api.notice.Formatter;
import online.popopo.api.notice.Notice;
import online.popopo.api.notice.UserNotice.PlayerNotice;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class VoteHandler {
    private final Plugin plugin;
    private final Vote vote;
    private final Formatter formatter;
    private final Map<Player, Ballot> ballots;

    public VoteHandler(Plugin p, Vote v, Formatter f) {
        this.plugin = p;
        this.vote = v;
        this.formatter = f;
        this.ballots = new HashMap<>();
    }

    public Ballot getBallot(Player p) {
        return ballots.get(p);
    }

    public boolean isFinished() {
        for (Ballot b : ballots.values()) {
            if (b.getState() != Ballot.FINISHED) {
                return false;
            }
        }

        return true;
    }

    public void start() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerNotice n = Notice.create(formatter, p);
            Ballot b = new Ballot(plugin, n, vote);

            b.open();
            this.ballots.put(p, b);
        }
    }

    public void stop() {
        Notice n = Notice.create(formatter);

        for (Ballot b : ballots.values()) {
            b.close(false);
        }

        n.good("Done", "Voting was finished!");
        vote.showResult(n);
    }
}
