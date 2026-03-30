package WebScraper.src.main.java.com.webscraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class WebScraper {
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final List<WebPage> scrapedPages = new ArrayList<>();
    private final List<ScrapedLink> scrapedLinks = new ArrayList<>();
    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";
    private int timeout = 5000;

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public WebPage scrapePage(String url) {
        try {
            Connection.Response response = fetch(url);
            Document document = response.parse();

            WebPage page = new WebPage(
                    response.url().toString(),
                    document.title(),
                    document.body() == null ? "" : document.body().text(),
                    response.statusCode(),
                    LocalDateTime.now().format(TIMESTAMP_FORMAT)
            );
            scrapedPages.add(page);

            System.out.println("Successfully scraped page: " + page.getTitle());
            return page;
        } catch (IOException e) {
            System.out.println("Failed to scrape page: " + e.getMessage());
            return null;
        }
    }

    public List<ScrapedLink> scrapeLinks(String url) {
        try {
            Document document = fetch(url).parse();
            List<ScrapedLink> links = new ArrayList<>();

            for (Element element : document.select("a[href]")) {
                String text = element.text().trim();
                String href = element.attr("abs:href").trim();

                if (!href.isEmpty()) {
                    ScrapedLink link = new ScrapedLink(
                            text.isEmpty() ? "(no text)" : text,
                            href,
                            url
                    );
                    links.add(link);
                    scrapedLinks.add(link);
                }
            }

            return links;
        } catch (IOException e) {
            System.out.println("Failed to scrape links: " + e.getMessage());
            return List.of();
        }
    }

    public List<String> scrapeImages(String url) {
        try {
            Document document = fetch(url).parse();
            List<String> images = new ArrayList<>();

            for (Element element : document.select("img[src]")) {
                String src = element.attr("abs:src").trim();
                if (!src.isEmpty()) {
                    images.add(src);
                }
            }

            return images;
        } catch (IOException e) {
            System.out.println("Failed to scrape images: " + e.getMessage());
            return List.of();
        }
    }

    public Map<String, Integer> scrapeHeadings(String url) {
        try {
            Document document = fetch(url).parse();
            Map<String, Integer> headings = new LinkedHashMap<>();

            for (int i = 1; i <= 6; i++) {
                int count = document.select("h" + i).size();
                if (count > 0) {
                    headings.put("H" + i, count);
                }
            }

            return headings;
        } catch (IOException e) {
            System.out.println("Failed to scrape headings: " + e.getMessage());
            return Map.of();
        }
    }

    public List<String> scrapeParagraphs(String url) {
        try {
            Document document = fetch(url).parse();
            List<String> paragraphs = new ArrayList<>();

            for (Element element : document.select("p")) {
                String text = element.text().trim();
                if (!text.isEmpty()) {
                    paragraphs.add(text);
                }
            }

            return paragraphs;
        } catch (IOException e) {
            System.out.println("Failed to scrape paragraphs: " + e.getMessage());
            return List.of();
        }
    }

    public Map<String, String> scrapeMeta(String url) {
        try {
            Document document = fetch(url).parse();
            Map<String, String> metadata = new LinkedHashMap<>();

            metadata.put("Title", document.title());

            addMetaIfPresent(metadata, "Description", document.selectFirst("meta[name=description]"));
            addMetaIfPresent(metadata, "Keywords", document.selectFirst("meta[name=keywords]"));
            addMetaIfPresent(metadata, "OG Title", document.selectFirst("meta[property=og:title]"));

            return metadata;
        } catch (IOException e) {
            System.out.println("Failed to scrape metadata: " + e.getMessage());
            return Map.of();
        }
    }

    public List<String> scrapeTableData(String url) {
        try {
            Document document = fetch(url).parse();
            List<String> rows = new ArrayList<>();

            for (Element table : document.select("table")) {
                for (Element row : table.select("tr")) {
                    Elements cells = row.select("th, td");
                    if (cells.isEmpty()) {
                        continue;
                    }

                    List<String> values = new ArrayList<>();
                    for (Element cell : cells) {
                        values.add(cell.text().trim());
                    }
                    rows.add(String.join(" | ", values));
                }
            }

            return rows;
        } catch (IOException e) {
            System.out.println("Failed to scrape table data: " + e.getMessage());
            return List.of();
        }
    }

    public void displayScrapedPages() {
        if (scrapedPages.isEmpty()) {
            System.out.println("No pages scraped yet.");
            return;
        }

        System.out.println("\nScraped Pages");
        System.out.println("=".repeat(60));
        for (int i = 0; i < scrapedPages.size(); i++) {
            WebPage page = scrapedPages.get(i);
            System.out.println((i + 1) + ". " + page.getTitle());
            System.out.println("   URL: " + page.getUrl());
            System.out.println("   Status: " + page.getStatusCode());
        }
    }

    public void displayScrapedLinks() {
        if (scrapedLinks.isEmpty()) {
            System.out.println("No links scraped yet.");
            return;
        }

        System.out.println("\nScraped Links");
        System.out.println("=".repeat(90));
        for (int i = 0; i < scrapedLinks.size(); i++) {
            ScrapedLink link = scrapedLinks.get(i);
            System.out.printf("%2d. %-35s %s%n", i + 1, abbreviate(link.getText(), 35), link.getHref());
        }
    }

    public void exportScrapedData(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("WEB SCRAPING REPORT\n");
            writer.write("=".repeat(60));
            writer.write("\nGenerated: " + LocalDateTime.now().format(TIMESTAMP_FORMAT) + "\n\n");

            writer.write("SCRAPED PAGES\n");
            writer.write("-".repeat(60) + "\n");
            for (WebPage page : scrapedPages) {
                writer.write(page.toString());
                writer.write("\n\n");
            }

            writer.write("SCRAPED LINKS\n");
            writer.write("-".repeat(60) + "\n");
            for (ScrapedLink link : scrapedLinks) {
                writer.write(link.toString());
                writer.write("\n");
            }

            System.out.println("Report exported to " + filename);
        } catch (IOException e) {
            System.out.println("Failed to export report: " + e.getMessage());
        }
    }

    public int getTotalPagesScraped() {
        return scrapedPages.size();
    }

    public int getTotalLinksScraped() {
        return scrapedLinks.size();
    }

    public void displayMenu() {
        System.out.println("\n========== Web Scraper ==========");
        System.out.println("1. Scrape full page");
        System.out.println("2. Scrape links");
        System.out.println("3. Scrape images");
        System.out.println("4. Scrape headings");
        System.out.println("5. Scrape paragraphs");
        System.out.println("6. Scrape metadata");
        System.out.println("7. Scrape tables");
        System.out.println("8. View scraped pages");
        System.out.println("9. View scraped links");
        System.out.println("10. Export data");
        System.out.println("11. Statistics");
        System.out.println("12. Exit");
        System.out.print("Enter choice: ");
    }

    private Connection.Response fetch(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent(userAgent)
                .timeout(timeout)
                .followRedirects(true)
                .ignoreHttpErrors(true)
                .execute();
    }

    private void addMetaIfPresent(Map<String, String> metadata, String key, Element metaElement) {
        if (metaElement != null) {
            String content = metaElement.attr("content").trim();
            if (!content.isEmpty()) {
                metadata.put(key, content);
            }
        }
    }

    private static String abbreviate(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength - 3) + "...";
    }

    private static int getValidChoice(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 12) {
                    return choice;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.print("Enter a number between 1 and 12: ");
        }
    }

    private static void printList(String heading, List<String> values, int limit) {
        System.out.println("\n" + heading);
        if (values.isEmpty()) {
            System.out.println("No data found.");
            return;
        }

        for (int i = 0; i < Math.min(limit, values.size()); i++) {
            System.out.println((i + 1) + ". " + values.get(i));
        }
    }

    private static void printMap(String heading, Map<String, ?> values) {
        System.out.println("\n" + heading);
        if (values.isEmpty()) {
            System.out.println("No data found.");
            return;
        }

        for (Map.Entry<String, ?> entry : values.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        WebScraper scraper = new WebScraper();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("Welcome to the JSoup Web Scraper");

        while (running) {
            scraper.displayMenu();
            int choice = getValidChoice(scanner);

            switch (choice) {
                case 1:
                    System.out.print("Enter URL: ");
                    WebPage page = scraper.scrapePage(scanner.nextLine().trim());
                    if (page != null) {
                        System.out.println("\n" + page);
                    }
                    break;
                case 2:
                    System.out.print("Enter URL: ");
                    List<ScrapedLink> links = scraper.scrapeLinks(scanner.nextLine().trim());
                    if (links.isEmpty()) {
                        System.out.println("No links found.");
                    } else {
                        System.out.println("\nLinks found: " + links.size());
                        for (int i = 0; i < Math.min(10, links.size()); i++) {
                            System.out.println((i + 1) + ". " + links.get(i));
                        }
                    }
                    break;
                case 3:
                    System.out.print("Enter URL: ");
                    printList("Images", scraper.scrapeImages(scanner.nextLine().trim()), 10);
                    break;
                case 4:
                    System.out.print("Enter URL: ");
                    printMap("Heading Counts", scraper.scrapeHeadings(scanner.nextLine().trim()));
                    break;
                case 5:
                    System.out.print("Enter URL: ");
                    printList("Paragraphs", scraper.scrapeParagraphs(scanner.nextLine().trim()), 5);
                    break;
                case 6:
                    System.out.print("Enter URL: ");
                    printMap("Metadata", scraper.scrapeMeta(scanner.nextLine().trim()));
                    break;
                case 7:
                    System.out.print("Enter URL: ");
                    printList("Table Rows", scraper.scrapeTableData(scanner.nextLine().trim()), 10);
                    break;
                case 8:
                    scraper.displayScrapedPages();
                    break;
                case 9:
                    scraper.displayScrapedLinks();
                    break;
                case 10:
                    System.out.print("Enter export filename: ");
                    scraper.exportScrapedData(scanner.nextLine().trim());
                    break;
                case 11:
                    System.out.println("\nStatistics");
                    System.out.println("Pages scraped: " + scraper.getTotalPagesScraped());
                    System.out.println("Links scraped: " + scraper.getTotalLinksScraped());
                    break;
                case 12:
                    System.out.println("Closing scraper.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }

        scanner.close();
    }
}
