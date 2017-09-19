package online.popopo.popopo.world;

import online.popopo.common.command.Command;
import online.popopo.common.command.NameGetter;
import online.popopo.common.command.SubCommand;
import online.popopo.common.command.ValueGetter;
import online.popopo.common.message.Notice;
import online.popopo.common.message.UserNotice.PlayerNotice;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Collectors;

public class TransferCommand implements Command {
    @SubCommand()
    public void transfer(Notice n, World w) {
        if (!(n instanceof PlayerNotice)) {
            n.bad("Error", "Can't used except player");

            return;
        }

        Player p = ((PlayerNotice) n).getPlayer();
        long start = System.nanoTime();

        p.teleport(w.getSpawnLocation());

        long end = System.nanoTime();
        float time = (end - start) / 1000000f;
        String s = String.format("%.4f", time);

        n.good("Done", "Teleported (" + s + "ms)");
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
