package online.popopo.popopo.protection;

import online.popopo.common.PluginBase;
import online.popopo.common.command.Command;
import online.popopo.common.command.NameGetter;
import online.popopo.common.command.SubCommand;
import online.popopo.common.command.ValueGetter;
import online.popopo.common.message.Notice;
import online.popopo.common.message.UserNotice.PlayerNotice;
import online.popopo.common.selection.AreaSelector;
import online.popopo.common.selection.Cuboid;
import online.popopo.popopo.protection.Reserve.Priority;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProtectCommand implements Command {
    private final AreaSelector selector;
    private final Map<String, Reserve> reserves;
    private final Map<String, License> licenses;

    public ProtectCommand(PluginBase p, Judge j) {
        this.selector = p.getSelector();
        this.reserves = j.getReserves();
        this.licenses = j.getLicenses();
    }

    @SubCommand(name = "create")
    public void add(Notice n, String name) {
        if (!(n instanceof PlayerNotice)) {
            n.bad("Error", "Can't used except player");

            return;
        } else if (reserves.containsKey(name)) {
            n.bad("Error", "Reserve already exists");

            return;
        }

        Player p = ((PlayerNotice) n).getPlayer();
        Cuboid s = selector.getSelectedArea(p);

        if (s == null) {
            n.bad("Error", "Please select cuboid area");
        } else {
            reserves.put(name, new Reserve(name, s));
            n.good("Done", "Reserve was added");
        }
    }

    @SubCommand(name = "delete")
    public void remove(Notice n, Reserve r) {
        reserves.remove(r.getName());
        n.good("Done", "Reserve was removed");
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

    @SubCommand(name = "license")
    public void license(Notice n, Reserve r, License l) {
        r.setLicense(l);
        n.good("Done", "License was updated");
    }

    @SubCommand(name = "list")
    public void showList(Notice n) {
        if (reserves.isEmpty()) {
            n.info("Info", "Reserves doesn't exist");
        } else {
            n.good("Info", "Reserve list");

            for (Reserve r : reserves.values()) {
                String name = r.getName();
                World w = r.getArea().getWorld();
                String msg
                        = "It's in " + w.getName()
                        + ". The protect set is "
                        + r.getLicense()
                        + ". (" + r.getPriority() +")";

                n.info(name, msg);
            }
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
