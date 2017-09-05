package online.popopo.popopo.selection;

import online.popopo.common.command.Argument;
import online.popopo.common.command.Definition;
import online.popopo.common.command.Executor;
import online.popopo.common.message.Caster;
import online.popopo.common.message.Caster.PlayerCaster;
import online.popopo.common.selection.AreaSelector;
import org.bukkit.entity.Player;

public class SelectCommand implements Definition {
    private final AreaSelector selector;

    public SelectCommand(AreaSelector s) {
        this.selector = s;
    }

    @Executor("enter")
    public void onEnterCommand(Caster c, Argument arg) {
        if (!(c instanceof PlayerCaster)) {
            c.bad("Error", "Can't used except player");

            return;
        }

        Player p = (Player) c.getTarget();

        selector.enableSelectionMode(p);
        c.info("Info", "Entered into selection mode");
    }

    @Executor("exit")
    public void onExitCommand(Caster c, Argument arg) {
        if (!(c instanceof PlayerCaster)) {
            c.bad("Error", "Can't used except player");

            return;
        }

        Player p = (Player) c.getTarget();

        selector.disableSelectionMode(p);
        c.info("Info", "Exited from selection mode");
    }

    @Override
    public String getCommand() {
        return "select";
    }
}
