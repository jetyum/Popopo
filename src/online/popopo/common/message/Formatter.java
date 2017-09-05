package online.popopo.common.message;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {
    private static final String BOLD_KEY = "**";
    private static final String STRIKETHROUGH_KEY = "~~";
    private static final String HIGHLIGHT_KEY = "==";
    private static final String UNDERLINE_KEY = "++";
    private static final String ITALIC_KEY = "*";
    private static final String ESCAPE_CHAR = "\\";

    private static final Pattern pattern
            = Pattern.compile("(\\\\?)" +
            "(\\*\\*|\\*|~~|==|\\+\\+)(.+?)\\1\\2");

    private final Theme theme;

    public Formatter(Theme t) {
        this.theme = t;
    }

    public Theme getTheme() {
        return theme;
    }

    private ChatColor getStyleFrom(String key) {
        switch (key) {
            case BOLD_KEY:
                return ChatColor.BOLD;
            case STRIKETHROUGH_KEY:
                return ChatColor.STRIKETHROUGH;
            case HIGHLIGHT_KEY:
                return theme.getHighlight();
            case UNDERLINE_KEY:
                return ChatColor.UNDERLINE;
            case ITALIC_KEY:
                return ChatColor.ITALIC;
            default:
                return null;
        }
    }

    public String format(String msg, String style) {
        Matcher m = pattern.matcher(msg);
        String ret = style + msg;

        while (m.find()) {
            String src = m.group(0);
            String esc = m.group(1);
            String key = m.group(2);
            String text = m.group(3);
            String dst;

            if (esc.equals(ESCAPE_CHAR)) {
                dst = src.replace(esc + key, key);
            } else {
                ChatColor c = getStyleFrom(key);

                dst = format(text, style + c);
            }

            dst += ChatColor.RESET + style;
            ret = ret.replace(src, dst);
        }

        return ret;
    }

    public String text(String text) {
        return format(text, theme.getText().toString());
    }

    public String standard(String chat) {
        return format(chat, "");
    }

    public String prefix(String prefix, ChatColor c) {
        String s = "[" + prefix + "]" + ChatColor.RESET;

        return format(s, c.toString());
    }
}
