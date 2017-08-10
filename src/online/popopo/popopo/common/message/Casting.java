package online.popopo.popopo.common.message;

public interface Caster {
    void cast(String message);

    void info(String title, String message);

    void good(String title, String message);

    void bad(String title, String message);

    void warning(String title, String message);
}
