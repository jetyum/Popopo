package online.popopo.popopo.protection;

import online.popopo.api.message.Guideable;
import online.popopo.api.selection.Cuboid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Reserve implements Serializable, Guideable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final Cuboid area;

    private String license = null;
    private Priority priority = Priority.NORMAL;
    private Set<String> members = new HashSet<>();

    public Reserve(String name, Cuboid c) {
        this.name = name;
        this.area = c;
    }

    public String getName() {
        return name;
    }

    public Cuboid getArea() {
        return area;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(License l) {
        license = l.getName();
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority p) {
        this.priority = p;
    }

    public Set<String> getMembers() {
        return members;
    }

    @Override
    public String getLoreTitle() {
        return "Information of Reserve";
    }

    @Override
    public List<String> getLore() {
        List<String> list = new ArrayList<>();

        list.add("world : " + area.getWorld().getName());
        list.add("size : " + area.getVolume());
        list.add("license : " + license);
        list.add("member :");

        for (String s : members) {
            list.add("  " + s);
        }

        list.add("priority : " + priority);

        return list;
    }

    public enum Priority {
        HIGHEST, ABOVE_NORMAL, NORMAL, BELOW_NORMAL, LOWEST
    }
}
