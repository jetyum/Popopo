package online.popopo.common.message;

import online.popopo.common.message.UserNotice.BlockNotice;
import online.popopo.common.message.UserNotice.ConsoleNotice;
import online.popopo.common.message.UserNotice.PlayerNotice;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public abstract class Notice {
    private final Formatter formatter;

    public Notice(Formatter f) {
        this.formatter = f;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    private Theme getTheme() {
        return formatter.getTheme();
    }

    private void send(ChatColor c, String p, String m) {
        String prefix = formatter.prefix(p, c);
        String msg = formatter.text(m);

        send(prefix + " " + msg);
    }

    public void info(String prefix, String msg){
        send(getTheme().getInfo(), prefix, msg);
    }

    public void good(String prefix, String msg){
        send(getTheme().getGood(), prefix, msg);
    }

    public void bad(String prefix, String msg){
        send(getTheme().getBad(), prefix, msg);
    }

    public void warning(String prefix, String msg){
        send(getTheme().getWarning(), prefix, msg);
    }

    public void guide(String name, Guideable g) {
        String t = g.getLoreTitle();

        send(getTheme().getHighlight(), name, t);

        for (String l : g.getLore()) {
            send(formatter.text("  " + l));
        }
    }

    public abstract void send(String msg);

    public static ServerNotice create(Formatter f) {
        return new ServerNotice(f);
    }

    public static PlayerNotice create(Formatter f,
                                      Player p) {
        return new PlayerNotice(f, p);
    }

    public static BlockNotice create(Formatter f,
                                     BlockCommandSender b) {
        return new BlockNotice(f, b);
    }

    public static ConsoleNotice create(Formatter f,
                                       ConsoleCommandSender c) {
        return new ConsoleNotice(f, c);
    }

    public static Notice create(Formatter f,
                                CommandSender s) {
        if (s instanceof Player) {
            return create(f, (Player) s);
        } else if (s instanceof BlockCommandSender) {
            return create(f, (BlockCommandSender) s);
        } else if (s instanceof ConsoleCommandSender) {
            return create(f, (ConsoleCommandSender) s);
        } else {
            return create(f);
        }
    }
}