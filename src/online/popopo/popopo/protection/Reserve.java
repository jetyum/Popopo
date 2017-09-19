package online.popopo.popopo.protection;

import online.popopo.common.selection.Cuboid;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Reserve implements Serializable {
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

    public enum Priority {
        HIGHEST, ABOVE_NORMAL, NORMAL, BELOW_NORMAL, LOWEST
    }
}
