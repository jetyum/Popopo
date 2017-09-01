package online.popopo.popopo.common.message;

public interface Casting {
    void info(String prefix, String msg);

    void good(String prefix, String msg);

    void bad(String prefix, String msg);

    void warning(String prefix, String msg);
}