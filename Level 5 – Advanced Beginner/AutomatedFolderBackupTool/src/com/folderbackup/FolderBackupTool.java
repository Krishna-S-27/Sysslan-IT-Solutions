package com.folderbackup;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class FolderBackupTool {
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static void main(String[] args) {
        Path configPath = args.length > 0
                ? Paths.get(args[0]).toAbsolutePath().normalize()
                : Paths.get("backup-config.properties").toAbsolutePath().normalize();

        try {
            BackupConfig config = BackupConfig.load(configPath);
            FolderBackupTool tool = new FolderBackupTool();
            BackupReport report = tool.runBackup(config);

            String reportText = report.toDisplayText();
            System.out.println(reportText);
            tool.writeReport(config.getReportFile(), reportText);
            System.out.println("Backup finished successfully.");
        } catch (Exception exception) {
            System.err.println("Backup failed: " + exception.getMessage());
            exception.printStackTrace(System.err);
        }
    }

    public BackupReport runBackup(BackupConfig config) throws IOException {
        validateConfig(config);

        LocalDateTime startedAt = LocalDateTime.now();
        String timestamp = startedAt.format(TIMESTAMP_FORMAT);
        Path snapshotDirectory = config.getBackupDirectory().resolve("backup_" + timestamp);
        Path latestMirrorDirectory = config.getBackupDirectory().resolve("latest_backup");

        Files.createDirectories(snapshotDirectory);
        Files.createDirectories(latestMirrorDirectory);

        BackupReport report = new BackupReport(
                startedAt,
                config.getBackupType(),
                config.getSourceDirectory(),
                snapshotDirectory
        );

        if (config.getBackupType() == BackupType.FULL) {
            recreateMirror(latestMirrorDirectory);
        }

        copyFiles(config.getSourceDirectory(), snapshotDirectory, latestMirrorDirectory,
                config.getBackupType(), report);
        syncDeletedFiles(config.getSourceDirectory(), latestMirrorDirectory, report);

        if (config.isCreateZipArchive()) {
            Path zipPath = config.getBackupDirectory().resolve(snapshotDirectory.getFileName() + ".zip");
            zipDirectory(snapshotDirectory, zipPath);
            report.setZipArchive(zipPath);
        }

        report.markCompleted(LocalDateTime.now());
        return report;
    }

    private void validateConfig(BackupConfig config) throws IOException {
        if (!Files.isDirectory(config.getSourceDirectory())) {
            throw new IllegalArgumentException("Source directory does not exist: " + config.getSourceDirectory());
        }

        Files.createDirectories(config.getBackupDirectory());
        Path normalizedSource = config.getSourceDirectory().toRealPath();
        Path normalizedBackup = config.getBackupDirectory().toAbsolutePath().normalize();
        if (normalizedBackup.startsWith(normalizedSource)) {
            throw new IllegalArgumentException("Backup directory must not be inside source directory.");
        }
    }

    private void copyFiles(Path sourceDirectory, Path snapshotDirectory, Path latestMirrorDirectory,
                           BackupType backupType, BackupReport report) throws IOException {
        Files.walkFileTree(sourceDirectory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path relative = sourceDirectory.relativize(dir);
                Files.createDirectories(snapshotDirectory.resolve(relative));
                Files.createDirectories(latestMirrorDirectory.resolve(relative));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path relative = sourceDirectory.relativize(file);
                Path mirrorFile = latestMirrorDirectory.resolve(relative);
                boolean shouldCopy = backupType == BackupType.FULL || hasChanged(file, mirrorFile);

                if (shouldCopy) {
                    Path snapshotFile = snapshotDirectory.resolve(relative);
                    Files.createDirectories(snapshotFile.getParent());
                    Files.createDirectories(mirrorFile.getParent());
                    Files.copy(file, snapshotFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                    Files.copy(file, mirrorFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                    report.addProcessedFile(new BackupFile(
                            file,
                            relative,
                            attrs.size(),
                            attrs.lastModifiedTime().toInstant(),
                            true
                    ));
                } else {
                    report.incrementSkippedFiles();
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    private boolean hasChanged(Path sourceFile, Path mirrorFile) throws IOException {
        if (!Files.exists(mirrorFile)) {
            return true;
        }

        BasicFileAttributes sourceAttributes = Files.readAttributes(sourceFile, BasicFileAttributes.class);
        BasicFileAttributes mirrorAttributes = Files.readAttributes(mirrorFile, BasicFileAttributes.class);

        return sourceAttributes.size() != mirrorAttributes.size()
                || sourceAttributes.lastModifiedTime().toMillis() != mirrorAttributes.lastModifiedTime().toMillis();
    }

    private void syncDeletedFiles(Path sourceDirectory, Path latestMirrorDirectory, BackupReport report) throws IOException {
        final Set<Path> sourceRelativePaths = new HashSet<Path>();
        Files.walkFileTree(sourceDirectory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                sourceRelativePaths.add(sourceDirectory.relativize(file));
                return FileVisitResult.CONTINUE;
            }
        });

        Files.walkFileTree(latestMirrorDirectory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path relative = latestMirrorDirectory.relativize(file);
                if (!sourceRelativePaths.contains(relative)) {
                    Files.deleteIfExists(file);
                    report.addDeletedFromMirror(relative);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (!dir.equals(latestMirrorDirectory) && isDirectoryEmpty(dir)) {
                    Files.deleteIfExists(dir);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private boolean isDirectoryEmpty(Path directory) throws IOException {
        java.util.stream.Stream<Path> stream = Files.list(directory);
        try {
            return !stream.findFirst().isPresent();
        } finally {
            stream.close();
        }
    }

    private void recreateMirror(Path latestMirrorDirectory) throws IOException {
        if (!Files.exists(latestMirrorDirectory)) {
            return;
        }

        Files.walkFileTree(latestMirrorDirectory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (!dir.equals(latestMirrorDirectory)) {
                    Files.deleteIfExists(dir);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void zipDirectory(Path sourceDirectory, Path zipPath) throws IOException {
        URI zipUri = URI.create("jar:" + zipPath.toUri());
        java.util.Map<String, String> environment = new java.util.HashMap<String, String>();
        environment.put("create", "true");

        if (Files.exists(zipPath)) {
            Files.delete(zipPath);
        }

        try (java.nio.file.FileSystem zipFileSystem = java.nio.file.FileSystems.newFileSystem(zipUri, environment)) {
            Files.walkFileTree(sourceDirectory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path relative = sourceDirectory.relativize(dir);
                    if (!relative.toString().isEmpty()) {
                        Files.createDirectories(zipFileSystem.getPath(relative.toString()));
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path relative = sourceDirectory.relativize(file);
                    Path target = zipFileSystem.getPath(relative.toString());
                    if (target.getParent() != null) {
                        Files.createDirectories(target.getParent());
                    }
                    Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    private void writeReport(Path reportFile, String reportText) throws IOException {
        if (reportFile.getParent() != null) {
            Files.createDirectories(reportFile.getParent());
        }
        try (BufferedWriter writer = Files.newBufferedWriter(reportFile)) {
            writer.write(reportText);
        }
    }
}
