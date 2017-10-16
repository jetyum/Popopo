package online.popopo.popopo.voting;

import online.popopo.api.MainBase;
import online.popopo.api.message.Formatter;
import online.popopo.api.message.Notice;
import online.popopo.api.message.UserNotice.PlayerNotice;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class VoteHandler {
    private final MainBase plugin;
    private final Vote vote;
    private final Map<Player, Ballot> ballots;

    public VoteHandler(MainBase p, Vote v) {
        this.plugin = p;
        this.vote = v;
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
        Formatter f = plugin.getFormatter();

        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerNotice n = Notice.create(f, p);
            Ballot b = new Ballot(plugin, n, vote);

            b.open();
            this.ballots.put(p, b);
        }
    }

    public void stop() {
        Notice n = Notice.create(plugin.getFormatter());

        for (Ballot b : ballots.values()) {
            b.close(false);
        }

        n.good("Done", "Voting was finished!");
        vote.showResult(n);
    }
}
