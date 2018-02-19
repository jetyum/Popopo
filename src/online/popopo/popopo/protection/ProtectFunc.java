package online.popopo.popopo.protection;

import online.popopo.api.function.Variable;
import online.popopo.api.wrapper.command.*;
import online.popopo.api.wrapper.listener.ListenerManager;
import online.popopo.api.io.Inject;
import online.popopo.api.io.Injector;
import online.popopo.api.io.tree.Config;
import online.popopo.api.io.tree.Data;
import online.popopo.api.notice.Notice;
import online.popopo.api.notice.UserNotice.PlayerNotice;
import online.popopo.api.function.Function;
import online.popopo.api.selection.AreaSelector;
import online.popopo.api.selection.Cuboid;
import online.popopo.popopo.protection.Reserve.Priority;
import online.popopo.popopo.protection.listener.BlockListener;
import online.popopo.popopo.protection.listener.EntityListener;
import online.popopo.popopo.protection.listener.ExplosionListener;
import online.popopo.popopo.protection.listener.PlayerListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Command(name = "protect")
public class ProtectFunc extends Function {
    @Variable
    private Plugin plugin;
    @Variable
    private ListenerManager listenerManager;
    @Variable
    private CommandManager commandManager;
    @Variable
    private AreaSelector selector;

    @Inject(key = "reserves")
    private final Map<String, Reserve> reserves;

    private final Map<String, License> licenses;

    public ProtectFunc() {
        this.reserves = new HashMap<>();
        this.licenses = new HashMap<>();
    }

    @Override
    public void load() {
        try {
            Data d = new Data(plugin, ".data/reserve.gz");
            d.load();
            Injector.inject(d, this);
        } catch (IOException e) {
            plugin.getLogger().info("Reserve wasn't loaded");
        }

        try {
            Config c = new Config(plugin, "license.yml");
            c.load();
            Injector.inject(c, licenses, License.class);
        } catch (IOException e) {
            plugin.getLogger().info("License wasn't loaded");
        }
    }

    @Override
    public void enable() {
        Judge judge = new Judge(reserves, licenses);

        listenerManager.register(
                new BlockListener(judge),
                new EntityListener(judge),
                new ExplosionListener(judge),
                new PlayerListener(judge)
        );
        commandManager.register(this);
    }

    @Override
    public void disable() {
        try {
            Data d = new Data(plugin, ".data/reserve.gz");
            Injector.inject(this, d);
            d.save();
        } catch (IOException e) {
            plugin.getLogger().info("Reserve wasn't saved");
        }
    }

    @SubCommand(name = "create")
    public void add(Notice n, String name) {
        if (!(n instanceof PlayerNotice)) {
            n.bad("Error", "Can't used except player");
        } else if (reserves.containsKey(name)) {
            n.bad("Error", "Reserve already exists");
        } else {
            Player p = ((PlayerNotice) n).getPlayer();
            Cuboid s = selector.getSelectedArea(p);

            if (s == null) {
                n.bad("Error", "Please select cuboid area");
            } else {
                reserves.put(name, new Reserve(name, s));
                n.good("Done", "Reserve was added");
            }
        }
    }

    @SubCommand(name = "delete")
    public void remove(Notice n, Reserve r) {
        reserves.remove(r.getName());
        n.good("Done", "Reserve was removed");
    }

    @SubCommand(name = "license")
    public void license(Notice n, Reserve r, License l) {
        r.setLicense(l);
        n.good("Done", "License was updated");
    }

    @SubCommand(name = "join")
    public void join(Notice n, Reserve r, String name) {
        r.getMembers().add(name);
        n.good("Done", "Member was joined");
    }

    @SubCommand(name = "defect")
    public void defect(Notice n, Reserve r, String name) {
        r.getMembers().remove(name);
        n.good("Done", "Member was defected");
    }

    @SubCommand(name = "priority")
    public void priority(Notice n, Reserve r, Priority p) {
        r.setPriority(p);
        n.good("Done", "Priority was updated");
    }

    @SubCommand(name = "list")
    public void list(Notice n) {
        if (reserves.isEmpty()) {
            n.info("Info", "Reserve doesn't exist");
        } else {
            n.info("Info", "Display a list of reserves");
            reserves.forEach(n::guide);
        }
    }

    @NameGetter(type = Reserve.class)
    public Set<String> getReserveNames() {
        return reserves.values().stream()
                .map(Reserve::getName)
                .collect(Collectors.toSet());
    }

    @NameGetter(type = License.class)
    public Set<String> getLicenseNames() {
        return licenses.values().stream()
                .map(License::getName)
                .collect(Collectors.toSet());
    }

    @NameGetter(type = Priority.class)
    public Set<String> getPriorityNames() {
        return Arrays.stream(Priority.values())
                .map(Priority::name)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @ValueGetter(type = Reserve.class)
    public Reserve getReserve(Notice n, String arg) {
        Reserve r = reserves.get(arg);

        if (r == null) {
            n.bad("Error", "Reserve doesn't exist");
        }

        return r;
    }

    @ValueGetter(type = License.class)
    public License getLicense(Notice n, String arg) {
        License l = licenses.get(arg);

        if (l == null) {
            n.bad("Error", "License doesn't exist");
        }

        return l;
    }

    @ValueGetter(type = Priority.class)
    public Priority getPriority(Notice n, String arg) {
        try {
            return Priority.valueOf(arg.toUpperCase());
        } catch (IllegalArgumentException e) {
            n.bad("Error", "Priority doesn't exist");

            return null;
        }
    }
}
