package com.webscraper;

public class ScrapedLink {
    private final String text;
    private final String href;
    private final String source;

    public ScrapedLink(String text, String href, String source) {
        this.text = text;
        this.href = href;
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public String getHref() {
        return href;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "Text: " + text + " | Link: " + href + " | Source: " + source;
    }
}
