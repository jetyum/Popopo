package online.popopo.popopo.common.config;

public interface Configuring {
    boolean load();

    boolean save();

    boolean contain(String section, String key);

    void set(String section, String key, Object value);

    Object get(String section, String key);

    boolean injectInto(Configurable i);

    boolean extractFrom(Configurable i);
}
