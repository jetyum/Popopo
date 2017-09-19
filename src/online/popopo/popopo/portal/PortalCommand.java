package online.popopo.popopo.portal;

import online.popopo.common.PluginBase;
import online.popopo.common.command.Command;
import online.popopo.common.command.NameGetter;
import online.popopo.common.command.SubCommand;
import online.popopo.common.command.ValueGetter;
import online.popopo.common.message.Notice;
import online.popopo.common.message.UserNotice.PlayerNotice;
import online.popopo.common.selection.AreaSelector;
import online.popopo.common.selection.Cuboid;
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
    public void create(Notice n, String name) {
        if (!(n instanceof PlayerNotice)) {
            n.bad("Error", "Can't used except player");

            return;
        } else if (portals.containsKey(name)) {
            n.bad("Error", "Portal already exists");

            return;
        }

        Player p = ((PlayerNotice) n).getPlayer();
        Cuboid s = selector.getSelectedArea(p);

        if (s == null) {
            n.bad("Error", "Please select cuboid area");
        } else {
            portals.put(name, new Portal(name, s));
            n.good("Done", "Portal was created");
        }
    }

    @SubCommand(name = "delete")
    public void delete(Notice n, Portal p) {
        portals.remove(p.getName());
        n.good("Done", "Portal was deleted");
    }

    @SubCommand(name = "connect")
    public void connect(Notice n, Portal from, Portal to) {
        from.setDestination(to.getName());
        n.good("Done", "Portal was connected");
    }

    @SubCommand(name = "disconnect")
    public void disconnect(Notice n, Portal p) {
        p.clearDestination();
        n.good("Done", "Portal was disconnected");
    }

    @SubCommand(name = "list")
    public void list(Notice n) {
        if (portals.isEmpty()) {
            n.info("Info", "Portal doesn't exist");
        } else {
            n.info("Info", "Display a list of portals");
            portals.forEach(n::guide);
        }
    }

    @NameGetter(type = Portal.class)
    public Set<String> getPortalNames() {
        return portals.values().stream()
                .map(Portal::getName)
                .collect(Collectors.toSet());
    }

    @ValueGetter(type = Portal.class)
    public Portal getPortal(Notice n, String arg) {
        Portal p = portals.get(arg);

        if (p == null) {
            n.bad("Error", "Portal doesn't exist");
        }

        return p;
    }
}
