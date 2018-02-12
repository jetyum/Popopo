package online.popopo.popopo.voting;

import online.popopo.api.command.Command;
import online.popopo.api.command.SubCommand;
import online.popopo.api.notice.Formatter;
import online.popopo.api.notice.Notice;
import online.popopo.api.notice.UserNotice.PlayerNotice;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

@Command(name = "vote")
public class VoteCommand implements Listener {
    private static final int PERIOD = 2400;

    private final Plugin plugin;
    private final Formatter formatter;

    private VoteHandler handler = null;

    public VoteCommand(Plugin p, Formatter f) {
        this.plugin = p;
        this.formatter = f;

        Bukkit.getPluginManager().registerEvents(this, p);
    }

    @SubCommand
    public void start(Notice n, String t, String... i) {
        if (!(n instanceof PlayerNotice)) {
            n.bad("Error", "Can't used except player");

            return;
        } else if (handler != null) {
            n.bad("Error", "Already started voting");

            return;
        }

        Player p = ((PlayerNotice) n).getPlayer();
        Vote v = new Vote(p, t, i);
        VoteHandler h = new VoteHandler(plugin, v, formatter);
        BukkitScheduler s = Bukkit.getScheduler();

        n.info("Info", "Start voting at now");
        h.start();
        handler = h;

        s.runTaskLater(plugin, () -> {
            if (h.equals(handler)) stop();
        }, PERIOD);
    }

    public void stop() {
        if (handler != null) {
            handler.stop();
            handler = null;
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (handler == null) return;

        Ballot b = handler.getBallot(e.getPlayer());

        if (b == null) return;

        b.close(true);

        if (handler.isFinished()) {
            stop();
        }
    }

    @EventHandler
    public void onScroll(PlayerItemHeldEvent e) {
        if (handler == null) return;

        Ballot b = handler.getBallot(e.getPlayer());

        if (b == null) return;

        int d = e.getNewSlot() - e.getPreviousSlot();

        if ((d > 0 && d != 8) || d == -8) {
            b.incrementIndex();
        } else {
            b.decrementIndex();
        }
    }
}
