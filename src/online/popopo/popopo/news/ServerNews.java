package online.popopo.popopo.news;

import online.popopo.api.io.Inject;
import online.popopo.api.notice.Guideable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ServerNews {
    @Inject(key = "number")
    private int number = 4;

    @Inject(key = "articles")
    private List<Map<String, String>> articles = null;

    public int getNumber() {
        return number;
    }

    public List<Article> getArticles() {
        List<Article> list = new ArrayList<>();

        if (articles == null) return list;

        for (Map<String, String> m : articles) {
            if (m.containsKey("title")) {
                String title = m.get("title");
                String body = m.getOrDefault("body", "");
                Article a = new Article(title, body);

                list.add(a);
            }
        }

        return list;
    }

    public class Article implements Guideable {
        private final String title;
        private final String body;

        Article(String title, String body) {
            this.title = title;
            this.body = body;
        }

        @Override
        public String getLoreTitle() {
            return title;
        }

        @Override
        public List<String> getLore() {
            return Collections.singletonList(body);
        }
    }
}
