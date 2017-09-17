package online.popopo.popopo.portal;

import online.popopo.common.PluginBase;
import online.popopo.common.command.Command;
import online.popopo.common.command.NameGetter;
import online.popopo.common.command.SubCommand;
import online.popopo.common.command.ValueGetter;
import online.popopo.common.message.Caster;
import online.popopo.common.message.Caster.PlayerCaster;
import online.popopo.common.selection.AreaSelector;
import online.popopo.common.selection.Cuboid;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PortalCommand implements Command {
    private final AreaSelector selector;
    private final Map<String, Portal> portals;

    public PortalCommand(PluginBase p, Map<String, Portal> m) {
        this.selector = p.getSelector();
        this.portals = m;
    }

    @SubCommand(name = "create")
    public void create(Caster c, String name) {
        if (!(c instanceof PlayerCaster)) {
            c.bad("Error", "Can't used except player");

            return;
        } else if (portals.containsKey(name)) {
            c.bad("Error", "Portal already exists");

            return;
        }

        Player p = ((PlayerCaster) c).getPlayer();
        Cuboid area = selector.getSelectedArea(p);

        if (area == null) {
            c.bad("Error", "Please select cuboid area");

            return;
        }

        portals.put(name, new Portal(name, area));
        c.good("Done", "Portal was created");
    }

    @SubCommand(name = "delete")
    public void delete(Caster c, Portal p) {
        portals.remove(p.getName());
        c.good("Done", "Portal was deleted");
    }

    @SubCommand(name = "connect")
    public void connect(Caster c, Portal from, Portal to) {
        from.setDestination(to.getName());
        c.good("Done", "Portal was connected");
    }

    @SubCommand(name = "disconnect")
    public void disconnect(Caster c, Portal p) {
        p.clearDestination();
        c.good("Done", "Portal was disconnected");
    }

    @SubCommand(name = "list")
    public void showList(Caster c) {
        if (portals.isEmpty()) {
            c.info("Info", "Portal doesn't exist");

            return;
        }

        c.good("Info", "Portal list");

        for (Portal p : portals.values()) {
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

    @NameGetter(type = Portal.class)
    public Set<String> getPortalNames() {
        return portals
                .values()
                .stream()
                .map(Portal::getName)
                .collect(Collectors.toSet());
    }

    @ValueGetter(type = Portal.class)
    public Portal getPortal(Caster c, String arg) {
        Portal p = portals.get(arg);

        if (p == null) {
            c.bad("Error", "Portal doesn't exist");
        }

        return p;
    }
}
