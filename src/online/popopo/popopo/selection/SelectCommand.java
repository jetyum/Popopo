package online.popopo.popopo.selection;

import online.popopo.common.PluginBase;
import online.popopo.common.command.Command;
import online.popopo.common.command.SubCommand;
import online.popopo.common.message.Notice;
import online.popopo.common.message.UserNotice.PlayerNotice;
import online.popopo.common.selection.AreaSelector;
import org.bukkit.entity.Player;

public class SelectCommand implements Command {
    private final AreaSelector selector;

    public SelectCommand(PluginBase p) {
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
