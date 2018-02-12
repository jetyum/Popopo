package online.popopo.api.notice;

import online.popopo.api.io.Inject;
import org.bukkit.ChatColor;

public class Theme {
    @Inject(key = "text")
    private ChatColor text = ChatColor.WHITE;

    @Inject(key = "highlight")
    private ChatColor highlight = ChatColor.WHITE;

    @Inject(key = "info")
    private ChatColor info = ChatColor.WHITE;

    @Inject(key = "good")
    private ChatColor good = ChatColor.WHITE;

    @Inject(key = "bad")
    private ChatColor bad = ChatColor.WHITE;

    @Inject(key = "warning")
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