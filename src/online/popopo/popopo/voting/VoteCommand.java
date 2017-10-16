package online.popopo.popopo.voting;

import online.popopo.api.MainBase;
import online.popopo.api.command.Command;
import online.popopo.api.command.SubCommand;
import online.popopo.api.message.Notice;
import online.popopo.api.message.UserNotice.PlayerNotice;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class VoteCommand implements Command {
    private static final int PERIOD = 2400;

    private final MainBase plugin;

    private VoteHandler handler = null;

    public VoteCommand(MainBase p) {
        this.plugin = p;

        Listener l = new VoteListener();

        Bukkit.getPluginManager().registerEvents(l, p);
    }

    @SubCommand
    public void start(Notice n, String t, String... c) {
        if (!(n instanceof PlayerNotice)) {
            n.bad("Error", "Can't used except player");

            return;
        } else if (handler != null) {
            n.bad("Error", "Already started voting");

            return;
        }

        Player p = ((PlayerNotice) n).getPlayer();
        Vote v = new Vote(p, t, c);
        BukkitScheduler s = Bukkit.getScheduler();

        n.info("Info", "Start voting at now");
        handler = new VoteHandler(plugin, v);
        handler.start();
        s.runTaskLater(plugin, this::stop, PERIOD);
    }

    public void stop() {
        if (handler != null) {
            handler.stop();
            handler = null;
        }
    }

    class VoteListener implements Listener {
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
}
