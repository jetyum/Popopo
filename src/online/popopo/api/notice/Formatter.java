package online.popopo.api.notice;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {
    private static final String PREFIX = "&";
    private static final String ESCAPE = "\\";

    private static final Pattern pattern
            = Pattern.compile("(\\\\?)&(.)(.+?)((?<!\\\\)&\\\\\\2|$)");

    private final Theme theme;

    public Formatter(Theme t) {
        this.theme = t;
    }

    public Theme getTheme() {
        return theme;
    }

    private String format(String s, String f, ChatColor c) {
        Matcher m = pattern.matcher(s);
        String ret = c + f + s;

        while (m.find()) {
            String src = m.group(0);
            String esc = m.group(1);
            String code = m.group(2);
            String text = m.group(3);
            ChatColor style = ChatColor.getByChar(code);
            String dst;

            if (style != null && !esc.equals(ESCAPE)) {
                if (style.isColor()) {
                    dst = format(text, f, style);
                } else {
                    dst = format(text, f + style, c);
                }
            } else {
                dst = src.replace(text, format(text, f, c));
            }

            ret = ret.replace(src, dst + c + f);
        }

        return ret;
    }

    public String format(String s, ChatColor c) {
        return format(s, "", c)
                .replace(ESCAPE + PREFIX, PREFIX);
    }

    public String text(String text) {
        return format(text, theme.getText());
    }

    public String standard(String chat) {
        return format(chat, ChatColor.RESET);
    }

    public String prefix(String prefix, ChatColor c) {
        String s = "[" + prefix + "]" + ChatColor.RESET;

        return format(s, c);
    }
}
