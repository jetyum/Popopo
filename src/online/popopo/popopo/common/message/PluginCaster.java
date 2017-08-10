package online.popopo.popopo.common.message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public abstract class PluginCaster implements Caster {
    private final Theme theme;

    public PluginCaster(Theme theme) {
        this.theme = theme;
    }

    protected String buildPrefix(ChatColor c, String s) {
        StringBuilder buf = new StringBuilder();

        buf.append("[");
        buf.append(c);
        buf.append(s);
        buf.append(ChatColor.RESET);
        buf.append("] ");

        return buf.toString();
    }

    @Override
    public void info(String title, String m){
        ChatColor info = this.theme.getInfo();
        String prefix = buildPrefix(info, title);

        cast(prefix + m);
    }

    @Override
    public void good(String title, String m){
        ChatColor good = this.theme.getGood();
        String prefix = buildPrefix(good, title);

        cast(prefix + m);
    }

    @Override
    public void bad(String title, String m){
        ChatColor bad = this.theme.getBad();
        String prefix = buildPrefix(bad, title);

        cast(prefix + m);
    }

    @Override
    public void warning(String title, String m){
        ChatColor warning = this.theme.getWarning();
        String prefix = buildPrefix(warning, title);

        cast(prefix + m);
    }

    public static class Messenger extends PluginCaster {
        private final CommandSender sender;

        public Messenger(Theme t, CommandSender s) {
            super(t);
            this.sender = s;
        }

        @Override
        public void cast(String m) {
            this.sender.sendMessage(m);
        }
    }

    public static class Broadcaster extends PluginCaster {
        public Broadcaster(Theme t) {
            super(t);
        }

        @Override
        public void cast(String m) {
            Bukkit.broadcastMessage(m);
        }
    }

    public static Messenger newFrom(Theme t, CommandSender s) {
        return new Messenger(t, s);
    }

    public static Broadcaster newFrom(Theme t) {
        return new Broadcaster(t);
    }
}
