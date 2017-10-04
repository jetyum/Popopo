package online.popopo.api.message;

import online.popopo.api.config.Property;
import org.bukkit.ChatColor;

public class Theme {
    @Property(key = "text")
    private ChatColor text = ChatColor.WHITE;

    @Property(key = "highlight")
    private ChatColor highlight = ChatColor.WHITE;

    @Property(key = "info")
    private ChatColor info = ChatColor.WHITE;

    @Property(key = "good")
    private ChatColor good = ChatColor.WHITE;

    @Property(key = "bad")
    private ChatColor bad = ChatColor.WHITE;

    @Property(key = "warning")
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