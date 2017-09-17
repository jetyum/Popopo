package online.popopo.popopo.protection;

import online.popopo.common.selection.Cuboid;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Reserve implements Serializable {
    private final String name;
    private final Cuboid area;
    private final String license;

    private Set<String> members = new HashSet<>();

    public Reserve(String name, Cuboid c, License l) {
        this.name = name;
        this.area = c;
        this.license = l.getName();
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

    public void addMember(String name) {
        members.add(name);
    }

    public void removeMember(String name) {
        members.remove(name);
    }

    public boolean hasMember(String name) {
        return members.contains(name);
    }
}
