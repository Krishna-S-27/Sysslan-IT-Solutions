package WebScraper.src.main.java.com.webscraper;

public class WebPage {
    private final String url;
    private final String title;
    private final String content;
    private final int statusCode;
    private final String scrapedAt;

    public WebPage(String url, String title, String content, int statusCode, String scrapedAt) {
        this.url = url;
        this.title = title;
        this.content = content;
        this.statusCode = statusCode;
        this.scrapedAt = scrapedAt;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getScrapedAt() {
        return scrapedAt;
    }

    @Override
    public String toString() {
        String preview = content.length() > 120 ? content.substring(0, 120) + "..." : content;
        return "URL: " + url
                + "\nTitle: " + title
                + "\nStatus: " + statusCode
                + "\nScraped At: " + scrapedAt
                + "\nContent Preview: " + preview;
    }
}
