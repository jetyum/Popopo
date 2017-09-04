package online.popopo.popopo.portal;

import online.popopo.common.command.Argument;
import online.popopo.common.command.Definition;
import online.popopo.common.command.Executor;
import online.popopo.common.message.Caster;
import online.popopo.common.message.Caster.PlayerCaster;
import online.popopo.common.selection.AreaSelector;
import online.popopo.common.selection.Cuboid;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PortalCommand implements Definition {
    private final AreaSelector selector;
    private final PortalList portals;

    public PortalCommand(AreaSelector s, PortalList p) {
        this.selector = s;
        this.portals = p;
    }

    @Executor({"create", "name"})
    public void onCreateCommand(Caster c, Argument arg) {
        if (!(c instanceof PlayerCaster)) {
            c.bad("Error", "Can't used except player");

            return;
        }

        String name = arg.get("name");

        if (portals.hasPortal(name)) {
            c.bad("Error", "Portal already exists");

            return;
        }

        Player p = ((PlayerCaster) c).getPlayer();
        Cuboid area = selector.getSelectedArea(p);

        if (area == null) {
            c.bad("Error", "Please select cuboid area");

            return;
        }

        portals.addPortal(new Portal(name, area));
        c.good("Done", "Portal was created");
    }

    @Executor({"delete", "name"})
    public void onDeleteCommand(Caster c, Argument arg) {
        String name = arg.get("name");

        if (!portals.hasPortal(name)) {
            c.bad("Error", "Portal doesn't exist");

            return;
        }

        portals.removePortal(name);
        c.good("Done", "Portal was deleted");
    }

    @Executor({"connect", "from", "to"})
    public void onConnectCommand(Caster c, Argument arg) {
        String fromName = arg.get("from");
        String toName = arg.get("to");

        if (!portals.hasPortal(fromName)) {
            c.bad("Error", "Departure portal doesn't exist");

            return;
        } else if (!portals.hasPortal(toName)) {
            c.bad("Error", "Arrival portal doesn't exist");

            return;
        }

        portals.getPortal(fromName).setDestination(toName);
        c.good("Done", "Portal was connected");
    }

    @Executor({"disconnect", "name"})
    public void onDisconnectCommand(Caster c, Argument arg) {
        String name = arg.get("name");

        if (!portals.hasPortal(name)) {
            c.bad("Error", "Portal doesn't exist");

            return;
        }

        portals.getPortal(name).clearDestination();
        c.good("Done", "Portal was disconnected");
    }

    @Executor({"list"})
    public void onListCommand(Caster c, Argument arg) {
        if (portals.getPortals().isEmpty()) {
            c.info("Info", "Portal doesn't exist");

            return;
        }

        c.good("Info", "Portal list");

        for (Portal p : portals.getPortals()) {
            String name = p.getName();
            World w = p.getArea().getWorld();
            String msg = "It's in " + w.getName();

            if (p.hasDestination()) {
                String to = p.getDestination();

                msg += " and be connected to " + to;
            }

            msg += ".";

            c.info(name, msg);
        }
    }

    @Override
    public String getCommand() {
        return "portal";
    }
}
