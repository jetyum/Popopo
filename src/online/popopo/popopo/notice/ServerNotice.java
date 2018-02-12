package online.popopo.popopo.notice;

import online.popopo.api.io.Inject;

import java.util.List;
import java.util.Map;

public class ServerNotice {
    @Inject(key = "news.number")
    private int newsNumber = 4;

    @Inject(key = "news.articles")
    private List<Map<String, String>> newsArticles = null;

    @Inject(key = "info.period")
    private int infoPeriod = 1200;

    @Inject(key = "info.articles")
    private List<Map<String, String>> infoArticles = null;

    public int getNewsNumber() {
        return newsNumber;
    }

    public List<Map<String, String>> getNewsArticles() {
        return newsArticles;
    }

    public int getInfoPeriod() {
        return infoPeriod;
    }

    public List<Map<String, String>> getInfoArticles() {
        return infoArticles;
    }
}
