package online.popopo.popopo.selection;

import online.popopo.api.MainBase;
import online.popopo.api.command.Command;
import online.popopo.api.command.SubCommand;
import online.popopo.api.message.Notice;
import online.popopo.api.message.UserNotice.PlayerNotice;
import online.popopo.api.selection.AreaSelector;
import org.bukkit.entity.Player;

public class SelectCommand implements Command {
    private final AreaSelector selector;

    public SelectCommand(MainBase p) {
        this.selector = p.getSelector();
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
