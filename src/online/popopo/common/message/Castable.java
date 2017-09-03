package online.popopo.common.message;

import org.bukkit.ChatColor;

public abstract class Castable {
    private final Theme theme;

    public Castable(Theme t) {
        this.theme = t;
    }

    public Theme getTheme() {
        return this.theme;
    }

    String decoratePrefix(ChatColor c, String p) {
        return c + "[" + p + "]" + ChatColor.RESET;
    }

    String decorateText(ChatColor c, String m) {
        return this.theme.getText() + m;
    }

    private void cast(ChatColor c, String p, String m) {
        String prefix = decoratePrefix(c, p);
        String msg = decorateText(c, m);

        cast(prefix + " " + msg);
    }

    public void info(String prefix, String msg){
        cast(this.theme.getInfo(), prefix, msg);
    }

    public void good(String prefix, String msg){
        cast(this.theme.getGood(), prefix, msg);
    }

    public void bad(String prefix, String msg){
        cast(this.theme.getBad(), prefix, msg);
    }

    public void warning(String prefix, String msg){
        cast(this.theme.getWarning(), prefix, msg);
    }

    public abstract void cast(String msg);
}