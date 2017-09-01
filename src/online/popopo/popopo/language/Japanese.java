package online.popopo.popopo.language;

import com.google.gson.Gson;
import online.popopo.popopo.common.config.Configurable;
import online.popopo.popopo.common.config.Parameter;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Japanese implements Converter, Configurable {
    @Parameter("table")
    private Map<String, String[]> table;

    @Parameter("url")
    private String url;

    @Parameter("pattern")
    private final Pattern pattern = null;

    private Reader requestJsonFrom(String kana) {
        try {
            String raw = kana + ",";
            String utf = URLEncoder.encode(raw, "UTF-8");
            URL url = new URL(this.url + utf);
            InputStream in = url.openStream();

            return new InputStreamReader(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> candidateOf(String kana) {
        Reader in = requestJsonFrom(kana);
        List<List<List<String>>> a = new ArrayList<>();

        a = new Gson().fromJson(in, a.getClass());
        IOUtils.closeQuietly(in);

        return new HashSet<>(a.get(0).get(1));
    }

    public String romaToKana(String roma) {
        StringBuilder buf = new StringBuilder();
        StringBuilder ret = new StringBuilder();

        for (char c : roma.toCharArray()) {
            buf.append(c);

            for (int i = 0; i < buf.length(); i++) {
                String key = buf.substring(i);

                if (this.table.containsKey(key)) {
                    String[] a = this.table.get(key);
                    String head = buf.substring(0, i);

                    ret.append(head).append(a[0]);
                    buf = new StringBuilder(a[1]);
                }
            }
        }

        return ret.append(buf).toString();
    }

    @Override
    public Set<String> convert(String s, boolean single) {
        Matcher m = this.pattern.matcher(s);
        Set<String> set = new HashSet<>();

        if (m.find()) {
            String base = s.substring(0, m.start());
            String kana = romaToKana(m.group());

            set.add(base + kana);

            if (!single) {
                for (String c : candidateOf(kana)) {
                    set.add(base + c);
                }

                set.addAll(Arrays.asList(s, ""));
            }
        }

        return set;
    }

    @Override
    public String getSectionName() {
        return "language.japanese";
    }
}
