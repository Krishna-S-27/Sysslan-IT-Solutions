package com.fileanalytics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ReportWriter {
    public void writeReports(Path outputDirectory, FileAnalyticsResult result, List<Map.Entry<String, Integer>> topWords)
            throws IOException {
        Files.createDirectories(outputDirectory);
        writeTextReport(outputDirectory.resolve("analysis-report.txt"), result, topWords);
        writeCsvSummary(outputDirectory.resolve("file-summary.csv"), result);
    }

    private void writeTextReport(Path path, FileAnalyticsResult result, List<Map.Entry<String, Integer>> topWords)
            throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("PERSONAL FILE ANALYTICS REPORT");
            writer.newLine();
            writer.write("Generated At : " + LocalDateTime.now());
            writer.newLine();
            writer.write("Files Scanned: " + result.getTotalFiles());
            writer.newLine();
            writer.write("Total Bytes  : " + result.getTotalBytes());
            writer.newLine();
            writer.newLine();

            writer.write("Extension Counts");
            writer.newLine();
            writer.write("----------------");
            writer.newLine();
            for (Map.Entry<String, Integer> entry : result.getExtensionCounts().entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }

            writer.newLine();
            writer.write("Top Words");
            writer.newLine();
            writer.write("---------");
            writer.newLine();
            for (Map.Entry<String, Integer> entry : topWords) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }

            writer.newLine();
            writer.write("Per File Summary");
            writer.newLine();
            writer.write("----------------");
            writer.newLine();
            for (FileSummary summary : result.getFileSummaries()) {
                writer.write(summary.getRelativePath() + " | " + summary.getExtension() + " | "
                        + summary.getBytes() + " bytes | lines=" + summary.getLines()
                        + " | words=" + summary.getWords() + " | characters=" + summary.getCharacters());
                writer.newLine();
            }
        }
    }

    private void writeCsvSummary(Path path, FileAnalyticsResult result) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("relativePath,extension,bytes,lines,words,characters");
            writer.newLine();
            for (FileSummary summary : result.getFileSummaries()) {
                writer.write(summary.getRelativePath() + "," + summary.getExtension() + ","
                        + summary.getBytes() + "," + summary.getLines() + ","
                        + summary.getWords() + "," + summary.getCharacters());
                writer.newLine();
            }
        }
    }
}
