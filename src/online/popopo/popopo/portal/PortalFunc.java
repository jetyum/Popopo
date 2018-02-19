package online.popopo.popopo.portal;

import online.popopo.api.function.Variable;
import online.popopo.api.function.command.*;
import online.popopo.api.function.listener.ListenerManager;
import online.popopo.api.io.Inject;
import online.popopo.api.io.Injector;
import online.popopo.api.io.tree.Data;
import online.popopo.api.notice.Notice;
import online.popopo.api.notice.UserNotice.PlayerNotice;
import online.popopo.api.function.Function;
import online.popopo.api.selection.AreaSelector;
import online.popopo.api.selection.Cuboid;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Command(name = "portal")
public class PortalFunc extends Function {
    @Variable
    private Plugin plugin;
    @Variable
    private ListenerManager listenerManager;
    @Variable
    private CommandManager commandManager;
    @Variable
    private AreaSelector selector;

    @Inject(key = "portals")
    private final Map<String, Portal> portals;

    public PortalFunc() {
        this.portals = new HashMap<>();
    }

    @Override
    public void load() {
        try {
            Data d = new Data(plugin, ".data/portal.gz");
            d.load();
            Injector.inject(d, this);
        } catch (IOException e) {
            plugin.getLogger().info("Portal wasn't loaded");
        }
    }

    @Override
    public void enable() {
        listenerManager.register(new PortalListener(portals));
        commandManager.register(this);
    }

    @Override
    public void disable() {
        try {
            Data d = new Data(plugin, ".data/portal.gz");
            Injector.inject(this, d);
            d.save();
        } catch (IOException e) {
            plugin.getLogger().info("Portal wasn't saved");
        }
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
