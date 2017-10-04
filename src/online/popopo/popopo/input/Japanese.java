package online.popopo.popopo.input;

import com.google.gson.Gson;
import online.popopo.api.config.Property;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Japanese implements Converter{
    private static final int CANDIDATE_NUM = 2;

    private static final Pattern pattern
            = Pattern.compile("\\p{ASCII}+$");

    @Property(key = "table")
    private Map<String, String[]> table;

    @Property(key = "url")
    private String url;

    private Reader requestJsonFrom(String kana) {
        try {
            String raw = kana + ",";
            String utf = URLEncoder.encode(raw, "UTF-8");
            InputStream in;

            in = new URL(url + utf).openStream();

            return new InputStreamReader(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> candidateOf(String kana) {
        Reader reader = requestJsonFrom(kana);
        List<List<List<String>>> a = new ArrayList<>();

        a = new Gson().fromJson(reader, a.getClass());
        IOUtils.closeQuietly(reader);

        List<String> list = a.get(0).get(1);
        boolean b = list.size() > CANDIDATE_NUM;
        int to = b ? CANDIDATE_NUM : list.size();

        return list.subList(0, to);
    }

    public String romaToKana(String roma) {
        StringBuilder buf = new StringBuilder();
        StringBuilder ret = new StringBuilder();

        for (char c : roma.toCharArray()) {
            buf.append(c);

            for (int i = 0; i < buf.length(); i++) {
                String key = buf.substring(i);

                if (table.containsKey(key)) {
                    String[] a = table.get(key);
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
        Matcher m = pattern.matcher(s);
        Set<String> set = new HashSet<>();

        if (m.find()) {
            String base = s.substring(0, m.start());
            String kana = romaToKana(m.group());

            set.add(base + kana);

            if (!single) {
                for (String c : candidateOf(kana)) {
                    set.add(base + c);
                }

                set.add("");
            }
        }

        return set;
    }
}
