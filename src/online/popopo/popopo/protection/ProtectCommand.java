package online.popopo.popopo.protection;

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

public class ProtectCommand implements Command {
    private final AreaSelector selector;
    private final Map<String, Reserve> reserves;
    private final Map<String, License> licenses;

    public ProtectCommand(AreaSelector s, Judge j) {
        this.selector = s;
        this.reserves = j.getReserves();
        this.licenses = j.getLicenses();
    }

    @SubCommand(name = "create")
    public void add(Caster c, String name, License l) {
        if (!(c instanceof PlayerCaster)) {
            c.bad("Error", "Can't used except player");

            return;
        } else if (reserves.containsKey(name)) {
            c.bad("Error", "Reserve already exists");

            return;
        }

        Player player = ((PlayerCaster) c).getPlayer();
        Cuboid area = selector.getSelectedArea(player);

        if (area == null) {
            c.bad("Error", "Please select cuboid area");

            return;
        }

        reserves.put(name, new Reserve(name, area, l));
        c.good("Done", "Reserve was added");
    }

    @SubCommand(name = "delete")
    public void remove(Caster c, Reserve r) {
        reserves.remove(r.getName());
        c.good("Done", "Reserve was removed");
    }

    @SubCommand(name = "join")
    public void join(Caster c, Reserve r, String name) {
        r.addMember(name);
        c.good("Done", "Member was joined");
    }

    @SubCommand(name = "defection")
    public void defection(Caster c, Reserve r, String name) {
        r.removeMember(name);
        c.good("Done", "Member was left");
    }

    @SubCommand(name = "list")
    public void showList(Caster c) {
        if (reserves.isEmpty()) {
            c.info("Info", "Reserves doesn't exist");

            return;
        }

        c.good("Info", "Reserve list");

        for (Reserve r : reserves.values()) {
            String name = r.getName();
            World w = r.getArea().getWorld();
            String msg
                    = "It's in " + w.getName() + ". "
                    + "The protect set is "
                    + r.getLicense() + ".";

            c.info(name, msg);
        }
    }

    @NameGetter(type = Reserve.class)
    public Set<String> getReserveNames() {
        return reserves
                .values().stream()
                .map(Reserve::getName)
                .collect(Collectors.toSet());
    }

    @NameGetter(type = License.class)
    public Set<String> getLicenseNames() {
        return licenses
                .values().stream()
                .map(License::getName)
                .collect(Collectors.toSet());
    }

    @ValueGetter(type = Reserve.class)
    public Reserve getReserve(Caster c, String arg) {
        Reserve r = reserves.get(arg);

        if (r == null) {
            c.bad("Error", "Reserve doesn't exist");
        }

        return r;
    }

    @ValueGetter(type = License.class)
    public License getLicense(Caster c, String arg) {
        License l = licenses.get(arg);

        if (l == null) {
            c.bad("Error", "License doesn't exist");
        }

        return l;
    }
}
