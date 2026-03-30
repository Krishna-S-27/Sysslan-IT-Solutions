package com.fileanalytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FileAnalyticsResult {
    private final List<FileSummary> fileSummaries = new ArrayList<FileSummary>();
    private final Map<String, Integer> extensionCounts;
    private final Map<String, Integer> wordFrequency;

    public FileAnalyticsResult(Map<String, Integer> extensionCounts, Map<String, Integer> wordFrequency) {
        this.extensionCounts = extensionCounts;
        this.wordFrequency = wordFrequency;
    }

    public void addFileSummary(FileSummary fileSummary) {
        fileSummaries.add(fileSummary);
    }

    public List<FileSummary> getFileSummaries() {
        return Collections.unmodifiableList(fileSummaries);
    }

    public Map<String, Integer> getExtensionCounts() {
        return extensionCounts;
    }

    public Map<String, Integer> getWordFrequency() {
        return wordFrequency;
    }

    public int getTotalFiles() {
        return fileSummaries.size();
    }

    public long getTotalBytes() {
        long total = 0L;
        for (FileSummary summary : fileSummaries) {
            total += summary.getBytes();
        }
        return total;
    }
}
