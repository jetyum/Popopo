package online.popopo.popopo.common;

import online.popopo.popopo.common.config.Configurable;
import online.popopo.popopo.common.config.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Messenger implements Configurable {
    @Parameter("info")
    private ChatColor info = ChatColor.WHITE;

    @Parameter("good")
    private ChatColor good = ChatColor.WHITE;

    @Parameter("bad")
    private ChatColor bad = ChatColor.WHITE;

    @Parameter("warning")
    private ChatColor warning = ChatColor.WHITE;

    private StringBuilder buildPrefix(ChatColor c, String s) {
        StringBuilder buf = new StringBuilder();

        buf.append("[");
        buf.append(c);
        buf.append(s);
        buf.append(ChatColor.RESET);
        buf.append("] ");

        return buf;
    }

    public void info(CommandSender s, String title, String m){
        StringBuilder buf = buildPrefix(this.info, title);

        s.sendMessage(buf.append(m).toString());
    }

    public void good(CommandSender s, String title, String m){
        StringBuilder buf = buildPrefix(this.good, title);

        s.sendMessage(buf.append(m).toString());
    }

    public void bad(CommandSender s, String title, String m){
        StringBuilder buf = buildPrefix(this.bad, title);

        s.sendMessage(buf.append(m).toString());
    }

    public void warning(CommandSender s, String title, String m){
        StringBuilder buf = buildPrefix(this.warning, title);

        s.sendMessage(buf.append(m).toString());
    }

    @Override
    public String getConfigName() {
        return "theme";
    }
}
