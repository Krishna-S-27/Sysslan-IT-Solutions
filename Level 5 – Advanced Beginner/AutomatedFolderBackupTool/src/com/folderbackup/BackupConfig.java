package com.folderbackup;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class BackupConfig {
    private final Path sourceDirectory;
    private final Path backupDirectory;
    private final BackupType backupType;
    private final boolean createZipArchive;
    private final Path reportFile;

    public BackupConfig(Path sourceDirectory, Path backupDirectory, BackupType backupType,
                        boolean createZipArchive, Path reportFile) {
        this.sourceDirectory = sourceDirectory;
        this.backupDirectory = backupDirectory;
        this.backupType = backupType;
        this.createZipArchive = createZipArchive;
        this.reportFile = reportFile;
    }

    public Path getSourceDirectory() {
        return sourceDirectory;
    }

    public Path getBackupDirectory() {
        return backupDirectory;
    }

    public BackupType getBackupType() {
        return backupType;
    }

    public boolean isCreateZipArchive() {
        return createZipArchive;
    }

    public Path getReportFile() {
        return reportFile;
    }

    public static BackupConfig load(Path configPath) throws IOException {
        Properties properties = new Properties();

        try (InputStream inputStream = Files.newInputStream(configPath)) {
            properties.load(inputStream);
        }

        Path source = resolvePath(configPath, require(properties, "source.directory"));
        Path backup = resolvePath(configPath, require(properties, "backup.directory"));
        Path report = resolvePath(configPath, properties.getProperty("report.file", "backup-report.txt"));
        BackupType type = BackupType.from(properties.getProperty("backup.type", "INCREMENTAL"));
        boolean createZip = Boolean.parseBoolean(properties.getProperty("create.zip", "true"));

        return new BackupConfig(source, backup, type, createZip, report);
    }

    private static String require(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required property: " + key);
        }
        return value.trim();
    }

    private static Path resolvePath(Path configPath, String rawPath) {
        Path path = Paths.get(rawPath);
        if (path.isAbsolute()) {
            return path.normalize();
        }

        Path parent = configPath.toAbsolutePath().getParent();
        if (parent == null) {
            return path.toAbsolutePath().normalize();
        }
        return parent.resolve(path).normalize();
    }
}
