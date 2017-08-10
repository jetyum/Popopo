package online.popopo.popopo.common.message;

public interface Castable {
    void cast(String m);

    void info(String title, String m);

    void good(String title, String m);

    void bad(String title, String m);

    void warning(String title, String m);
}
