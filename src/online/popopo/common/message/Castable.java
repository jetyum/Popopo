package online.popopo.common.message;

import org.bukkit.ChatColor;

public abstract class Castable {
    private final Formatter formatter;

    public Castable(Formatter f) {
        this.formatter = f;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    private Theme getTheme() {
        return formatter.getTheme();
    }

    private void cast(ChatColor c, String p, String m) {
        String prefix = formatter.prefix(p, c);
        String msg = formatter.text(m);

        cast(prefix + " " + msg);
    }

    public void info(String prefix, String msg){
        cast(getTheme().getInfo(), prefix, msg);
    }

    public void good(String prefix, String msg){
        cast(getTheme().getGood(), prefix, msg);
    }

    public void bad(String prefix, String msg){
        cast(getTheme().getBad(), prefix, msg);
    }

    public void warning(String prefix, String msg){
        cast(getTheme().getWarning(), prefix, msg);
    }

    public abstract void cast(String msg);
}