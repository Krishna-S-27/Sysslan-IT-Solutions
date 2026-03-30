package com.fileanalytics;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class FileAnalyticsProcessor {
    public FileAnalyticsResult analyze(Path inputDirectory) throws IOException {
        final Map<String, Integer> extensionCounts = new TreeMap<String, Integer>();
        final Map<String, Integer> wordFrequency = new LinkedHashMap<String, Integer>();
        final FileAnalyticsResult result = new FileAnalyticsResult(extensionCounts, wordFrequency);

        Files.walkFileTree(inputDirectory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String extension = getExtension(file.getFileName().toString());
                increment(extensionCounts, extension);

                if (isSupportedTextFile(extension)) {
                    String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
                    int lines = content.isEmpty() ? 0 : content.split("\\R").length;
                    int words = countWords(content);
                    int characters = content.length();
                    addWords(wordFrequency, content);
                    result.addFileSummary(new FileSummary(
                            inputDirectory.relativize(file),
                            extension,
                            attrs.size(),
                            lines,
                            words,
                            characters
                    ));
                }

                return FileVisitResult.CONTINUE;
            }
        });

        return result;
    }

    public List<Map.Entry<String, Integer>> topWords(Map<String, Integer> frequency, int limit) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(frequency.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> first, Map.Entry<String, Integer> second) {
                int valueCompare = Integer.compare(second.getValue(), first.getValue());
                return valueCompare != 0 ? valueCompare : first.getKey().compareTo(second.getKey());
            }
        });

        if (entries.size() > limit) {
            return entries.subList(0, limit);
        }
        return entries;
    }

    private boolean isSupportedTextFile(String extension) {
        return ".txt".equals(extension) || ".log".equals(extension) || ".csv".equals(extension) || ".md".equals(extension);
    }

    private int countWords(String content) {
        String trimmed = content.trim();
        if (trimmed.isEmpty()) {
            return 0;
        }
        return trimmed.split("\\s+").length;
    }

    private void addWords(Map<String, Integer> frequency, String content) {
        String normalized = content.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9\\s]", " ");
        for (String token : normalized.split("\\s+")) {
            if (token.length() < 3) {
                continue;
            }
            increment(frequency, token);
        }
    }

    private void increment(Map<String, Integer> map, String key) {
        Integer value = map.get(key);
        map.put(key, value == null ? 1 : value + 1);
    }

    private String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return "(no extension)";
        }
        return filename.substring(index).toLowerCase(Locale.ROOT);
    }
}
