package online.popopo.popopo.protection;

import online.popopo.api.MainBase;
import online.popopo.api.command.Command;
import online.popopo.api.command.NameGetter;
import online.popopo.api.command.SubCommand;
import online.popopo.api.command.ValueGetter;
import online.popopo.api.message.Notice;
import online.popopo.api.message.UserNotice.PlayerNotice;
import online.popopo.api.selection.AreaSelector;
import online.popopo.api.selection.Cuboid;
import online.popopo.popopo.protection.Reserve.Priority;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Command(name = "protect")
public class ProtectCommand {
    private final AreaSelector selector;
    private final Map<String, Reserve> reserves;
    private final Map<String, License> licenses;

    public ProtectCommand(MainBase p, Judge j) {
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
