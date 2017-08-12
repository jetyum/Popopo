package online.popopo.popopo.common.message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public abstract class Caster implements Casting {
    private final Theme theme;

    public Caster(Theme theme) {
        this.theme = theme;
    }

    protected String buildPrefix(ChatColor c, String s) {
        StringBuilder buf = new StringBuilder();

        buf.append(c);
        buf.append("[");
        buf.append(s);
        buf.append("] ");
        buf.append(ChatColor.RESET);

        return buf.toString();
    }

    @Override
    public void info(String title, String message){
        ChatColor info = this.theme.getInfo();
        String prefix = buildPrefix(info, title);

        cast(prefix + this.theme.getText() + message);
    }

    @Override
    public void good(String title, String message){
        ChatColor good = this.theme.getGood();
        String prefix = buildPrefix(good, title);

        cast(prefix + this.theme.getText() + message);
    }

    @Override
    public void bad(String title, String message){
        ChatColor bad = this.theme.getBad();
        String prefix = buildPrefix(bad, title);

        cast(prefix + this.theme.getText() + message);
    }

    @Override
    public void warning(String title, String message){
        ChatColor warning = this.theme.getWarning();
        String prefix = buildPrefix(warning, title);

        cast(prefix + this.theme.getText() + message);
    }

    public static class Messenger extends Caster {
        private final CommandSender sender;

        public Messenger(Theme t, CommandSender s) {
            super(t);
            this.sender = s;
        }

        @Override
        public void cast(String message) {
            this.sender.sendMessage(message);
        }
    }

    public static class Broadcaster extends Caster {
        public Broadcaster(Theme t) {
            super(t);
        }

        @Override
        public void cast(String message) {
            Bukkit.broadcastMessage(message);
        }
    }

    public static Messenger newFrom(Theme t, CommandSender s) {
        return new Messenger(t, s);
    }

    public static Broadcaster newFrom(Theme t) {
        return new Broadcaster(t);
    }
}
