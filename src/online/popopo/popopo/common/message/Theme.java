package online.popopo.popopo.common.message;

import online.popopo.popopo.common.config.Configurable;
import online.popopo.popopo.common.config.Parameter;
import org.bukkit.ChatColor;

public class Theme implements Configurable {
    @Override
    public String getSectionName() {
        return "theme";
    }

    @Parameter("info")
    private ChatColor info = ChatColor.WHITE;

    @Parameter("good")
    private ChatColor good = ChatColor.WHITE;

    @Parameter("bad")
    private ChatColor bad = ChatColor.WHITE;

    @Parameter("warning")
    private ChatColor warning = ChatColor.WHITE;

    public ChatColor getInfo() {
        return info;
    }

    public ChatColor getGood() {
        return good;
    }

    public ChatColor getBad() {
        return bad;
    }

    public ChatColor getWarning() {
        return warning;
    }
}