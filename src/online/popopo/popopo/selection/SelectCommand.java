package online.popopo.popopo.selection;

import online.popopo.common.command.Command;
import online.popopo.common.command.SubCommand;
import online.popopo.common.message.Caster;
import online.popopo.common.message.Caster.PlayerCaster;
import online.popopo.common.selection.AreaSelector;
import org.bukkit.entity.Player;

public class SelectCommand implements Command {
    private final AreaSelector selector;

    public SelectCommand(AreaSelector s) {
        this.selector = s;
    }

    @SubCommand(name = "enter")
    public void enter(Caster c) {
        if (!(c instanceof PlayerCaster)) {
            c.bad("Error", "Can't used except player");

            return;
        }

        Player p = (Player) c.getTarget();

        selector.enableSelectionMode(p);
        c.info("Info", "Entered into selection mode");
    }

    @SubCommand(name = "exit")
    public void exit(Caster c) {
        if (!(c instanceof PlayerCaster)) {
            c.bad("Error", "Can't used except player");

            return;
        }

        Player p = (Player) c.getTarget();

        selector.disableSelectionMode(p);
        c.info("Info", "Exited from selection mode");
    }
}
