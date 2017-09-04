package online.popopo.popopo.world;

import online.popopo.common.command.Argument;
import online.popopo.common.command.Definition;
import online.popopo.common.command.Executor;
import online.popopo.common.message.Caster;
import online.popopo.common.message.Caster.PlayerCaster;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TransferCommand implements Definition {
    @Executor({"", "world"})
    public void onCommand(Caster c, Argument arg) {
        if (!(c instanceof PlayerCaster)) {
            c.bad("Error", "Can't used except player");

            return;
        }

        String worldName = arg.get("world");

        if (Bukkit.getWorld(worldName) == null) {
            c.bad("Error", "World doesn't exist");

            return;
        }

        Player p = (Player) c.getTarget();
        World w = Bukkit.getWorld(worldName);

        long start = System.nanoTime();

        p.teleport(w.getSpawnLocation());

        long end = System.nanoTime();
        float time = (end - start) / 1000000f;

        c.good("Done", "Teleported (" + time + "ms)");
    }

    @Override
    public String getCommand() {
        return "transfer";
    }
}
