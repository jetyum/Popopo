package online.popopo.popopo.selection;

import online.popopo.api.function.Variable;
import online.popopo.api.wrapper.command.Command;
import online.popopo.api.wrapper.command.CommandManager;
import online.popopo.api.wrapper.command.SubCommand;
import online.popopo.api.notice.Notice;
import online.popopo.api.notice.UserNotice.PlayerNotice;
import online.popopo.api.function.Function;
import online.popopo.api.selection.AreaSelector;
import org.bukkit.entity.Player;

@Command(name = "select")
public class SelectFunc extends Function {
    @Variable
    private CommandManager commandManager;
    @Variable
    private AreaSelector selector;

    @Override
    public void enable() {
        commandManager.register(this);
    }

    @SubCommand()
    public void enter(Notice n) {
        if (!(n instanceof PlayerNotice)) {
            n.bad("Error", "Can't used except player");
        } else {
            Player p = ((PlayerNotice) n).getPlayer();

            selector.enableSelectionMode(p);
            n.info("Info", "Entered into selection mode");
        }
    }

    @SubCommand(name = "cancel")
    public void exit(Notice n) {
        if (!(n instanceof PlayerNotice)) {
            n.bad("Error", "Can't used except player");
        } else {
            Player p = ((PlayerNotice) n).getPlayer();

            selector.disableSelectionMode(p);
            n.info("Info", "Exited from selection mode");
        }
    }
}
