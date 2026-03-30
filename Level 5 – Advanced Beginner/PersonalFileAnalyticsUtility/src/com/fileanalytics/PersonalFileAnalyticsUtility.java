package com.fileanalytics;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class PersonalFileAnalyticsUtility {
    public static void main(String[] args) {
        Path configPath = args.length > 0
                ? Paths.get(args[0]).toAbsolutePath().normalize()
                : Paths.get("utility-config.properties").toAbsolutePath().normalize();

        try {
            UtilityConfig config = UtilityConfig.load(configPath);
            FileAnalyticsProcessor processor = new FileAnalyticsProcessor();
            FileAnalyticsResult result = processor.analyze(config.getInputDirectory());
            List<Map.Entry<String, Integer>> topWords =
                    processor.topWords(result.getWordFrequency(), config.getTopWordLimit());

            ReportWriter reportWriter = new ReportWriter();
            reportWriter.writeReports(config.getOutputDirectory(), result, topWords);

            System.out.println("Personal file analytics completed.");
            System.out.println("Files Scanned : " + result.getTotalFiles());
            System.out.println("Total Bytes   : " + result.getTotalBytes());
            System.out.println("Output Folder : " + config.getOutputDirectory());
            System.out.println("Top Words");
            for (Map.Entry<String, Integer> entry : topWords) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        } catch (Exception exception) {
            System.err.println("Utility task failed: " + exception.getMessage());
            exception.printStackTrace(System.err);
        }
    }
}
