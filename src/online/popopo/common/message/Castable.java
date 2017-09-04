package online.popopo.common.message;

import org.bukkit.ChatColor;

public abstract class Castable {
    private final Theme theme;

    public Castable(Theme t) {
        this.theme = t;
    }

    public Theme getTheme() {
        return theme;
    }

    String decoratePrefix(ChatColor c, String p) {
        return c + "[" + p + "]" + ChatColor.RESET;
    }

    String decorateText(ChatColor c, String m) {
        return theme.getText() + m;
    }

    private void cast(ChatColor c, String p, String m) {
        String prefix = decoratePrefix(c, p);
        String msg = decorateText(c, m);

        cast(prefix + " " + msg);
    }

    public void info(String prefix, String msg){
        cast(theme.getInfo(), prefix, msg);
    }

    public void good(String prefix, String msg){
        cast(theme.getGood(), prefix, msg);
    }

    public void bad(String prefix, String msg){
        cast(theme.getBad(), prefix, msg);
    }

    public void warning(String prefix, String msg){
        cast(theme.getWarning(), prefix, msg);
    }

    public abstract void cast(String msg);
}