package online.popopo.popopo.selection;

import online.popopo.api.command.Command;
import online.popopo.api.command.SubCommand;
import online.popopo.api.notice.Notice;
import online.popopo.api.notice.UserNotice.PlayerNotice;
import online.popopo.api.selection.AreaSelector;
import org.bukkit.entity.Player;

@Command(name = "select")
public class SelectCommand {
    private final AreaSelector selector;

    public SelectCommand(AreaSelector s) {
        this.selector = s;
    }

    @SubCommand()
    public void enter(Notice n) {
        if (!(n instanceof PlayerNotice)) {
            n.bad("Error", "Can't used except player");

            return;
        }

        Player p = ((PlayerNotice) n).getPlayer();

        selector.enableSelectionMode(p);
        n.info("Info", "Entered into selection mode");
    }

    @SubCommand(name = "cancel")
    public void exit(Notice n) {
        if (!(n instanceof PlayerNotice)) {
            n.bad("Error", "Can't used except player");

            return;
        }

        Player p = ((PlayerNotice) n).getPlayer();

        selector.disableSelectionMode(p);
        n.info("Info", "Exited from selection mode");
    }
}
