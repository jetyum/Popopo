package online.popopo.popopo.transfer;

import online.popopo.api.function.Variable;
import online.popopo.api.function.command.*;
import online.popopo.api.notice.Notice;
import online.popopo.api.notice.UserNotice.PlayerNotice;
import online.popopo.api.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Collectors;

@Command(name = "transfer")
public class TransferFunc extends Function {
    @Variable
    private CommandManager commandManager;

    @Override
    public void enable() {
        commandManager.register(this);
    }

    @SubCommand()
    public void transfer(Notice n, World w) {
        if (!(n instanceof PlayerNotice)) {
            n.bad("Error", "Can't used except player");
        } else {
            Player p = ((PlayerNotice) n).getPlayer();
            long start = System.nanoTime();

            p.teleport(w.getSpawnLocation());

            long end = System.nanoTime();
            float time = (end - start) / 1000000f;
            String s = String.format("%.4f", time);

            n.good("Done", "Teleported (" + s + "ms)");
        }
    }

    @NameGetter(type = World.class)
    public Set<String> getWorldNames() {
        return Bukkit
                .getWorlds()
                .stream()
                .map(World::getName)
                .collect(Collectors.toSet());
    }

    @ValueGetter(type = World.class)
    public World getWorld(Notice n, String arg) {
        World w = Bukkit.getWorld(arg);

        if (w == null) {
            n.bad("Error", "World doesn't exist");
        }

        return w;
    }
}
