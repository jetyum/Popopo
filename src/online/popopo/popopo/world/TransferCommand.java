package online.popopo.popopo.world;

import online.popopo.common.command.Command;
import online.popopo.common.command.NameGetter;
import online.popopo.common.command.SubCommand;
import online.popopo.common.command.ValueGetter;
import online.popopo.common.message.Caster;
import online.popopo.common.message.Caster.PlayerCaster;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Collectors;

public class TransferCommand implements Command {
    @SubCommand()
    public void transfer(Caster c, World w) {
        if (!(c instanceof PlayerCaster)) {
            c.bad("Error", "Can't used except player");

            return;
        }

        Player p = (Player) c.getTarget();
        long start = System.nanoTime();

        p.teleport(w.getSpawnLocation());

        long end = System.nanoTime();
        float time = (end - start) / 1000000f;

        c.good("Done", "Teleported (" + time + "ms)");
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
    public World getWorld(Caster c, String arg) {
        World w = Bukkit.getWorld(arg);

        if (w == null) {
            c.bad("Error", "World doesn't exist");
        }

        return w;
    }
}
