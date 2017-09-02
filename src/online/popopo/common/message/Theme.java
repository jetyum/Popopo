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

    @Parameter("info")
    private ChatColor info = ChatColor.WHITE;

    @Parameter("good")
    private ChatColor good = ChatColor.WHITE;

    @Parameter("bad")
    private ChatColor bad = ChatColor.WHITE;

    @Parameter("warning")
    private ChatColor warning = ChatColor.WHITE;

    public ChatColor getText() {
        return this.text;
    }

    public ChatColor getInfo() {
        return this.info;
    }

    public ChatColor getGood() {
        return this.good;
    }

    public ChatColor getBad() {
        return this.bad;
    }

    public ChatColor getWarning() {
        return this.warning;
    }
}