package online.popopo.common.message;

import online.popopo.common.config.Configurable;
import online.popopo.common.config.Parameter;
import org.bukkit.ChatColor;

public class Theme implements Configurable {
    @Override
    public String getSectionName() {
        return "theme";
    }

    @Parameter("text")
    private ChatColor text = ChatColor.WHITE;

    @Parameter("highlight")
    private ChatColor highlight = ChatColor.WHITE;

    @Parameter("info")
    private ChatColor info = ChatColor.WHITE;

    @Parameter("good")
    private ChatColor good = ChatColor.WHITE;

    @Parameter("bad")
    private ChatColor bad = ChatColor.WHITE;

    @Parameter("warning")
    private ChatColor warning = ChatColor.WHITE;

    public ChatColor getText() {
        return text;
    }

    public ChatColor getHighlight() {
        return highlight;
    }

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