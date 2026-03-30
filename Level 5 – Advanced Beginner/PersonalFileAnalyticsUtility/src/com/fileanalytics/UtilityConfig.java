package com.fileanalytics;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class UtilityConfig {
    private final Path inputDirectory;
    private final Path outputDirectory;
    private final int topWordLimit;

    public UtilityConfig(Path inputDirectory, Path outputDirectory, int topWordLimit) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
        this.topWordLimit = topWordLimit;
    }

    public Path getInputDirectory() {
        return inputDirectory;
    }

    public Path getOutputDirectory() {
        return outputDirectory;
    }

    public int getTopWordLimit() {
        return topWordLimit;
    }

    public static UtilityConfig load(Path configPath) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(configPath)) {
            properties.load(inputStream);
        }

        Path input = resolve(configPath, required(properties, "input.directory"));
        Path output = resolve(configPath, properties.getProperty("output.directory", "output"));
        int topWordLimit = Integer.parseInt(properties.getProperty("top.word.limit", "10"));
        return new UtilityConfig(input, output, topWordLimit);
    }

    private static String required(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required property: " + key);
        }
        return value.trim();
    }

    private static Path resolve(Path configPath, String rawPath) {
        Path path = Paths.get(rawPath);
        if (path.isAbsolute()) {
            return path.normalize();
        }
        Path parent = configPath.toAbsolutePath().getParent();
        return parent == null ? path.toAbsolutePath().normalize() : parent.resolve(path).normalize();
    }
}
