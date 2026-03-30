package com.folderbackup;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BackupReport {
    private final LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private final BackupType backupType;
    private final Path sourceDirectory;
    private final Path snapshotDirectory;
    private final List<BackupFile> processedFiles = new ArrayList<BackupFile>();
    private final List<Path> deletedFromMirror = new ArrayList<Path>();
    private int skippedFiles;
    private Path zipArchive;

    public BackupReport(LocalDateTime startedAt, BackupType backupType,
                        Path sourceDirectory, Path snapshotDirectory) {
        this.startedAt = startedAt;
        this.backupType = backupType;
        this.sourceDirectory = sourceDirectory;
        this.snapshotDirectory = snapshotDirectory;
    }

    public void addProcessedFile(BackupFile file) {
        processedFiles.add(file);
    }

    public void addDeletedFromMirror(Path relativePath) {
        deletedFromMirror.add(relativePath);
    }

    public void incrementSkippedFiles() {
        skippedFiles++;
    }

    public void markCompleted(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public void setZipArchive(Path zipArchive) {
        this.zipArchive = zipArchive;
    }

    public String toDisplayText() {
        StringBuilder builder = new StringBuilder();
        builder.append("AUTOMATED FOLDER BACKUP REPORT").append(System.lineSeparator());
        builder.append("Started At      : ").append(startedAt).append(System.lineSeparator());
        builder.append("Completed At    : ").append(completedAt).append(System.lineSeparator());
        builder.append("Duration        : ").append(getDuration().toMillis()).append(" ms").append(System.lineSeparator());
        builder.append("Backup Type     : ").append(backupType).append(System.lineSeparator());
        builder.append("Source Directory: ").append(sourceDirectory).append(System.lineSeparator());
        builder.append("Snapshot Folder : ").append(snapshotDirectory).append(System.lineSeparator());
        builder.append("Files Copied    : ").append(getCopiedFilesCount()).append(System.lineSeparator());
        builder.append("Files Skipped   : ").append(skippedFiles).append(System.lineSeparator());
        builder.append("Mirror Deletes  : ").append(deletedFromMirror.size()).append(System.lineSeparator());
        builder.append("Bytes Copied    : ").append(getCopiedBytes()).append(System.lineSeparator());
        if (zipArchive != null) {
            builder.append("ZIP Archive     : ").append(zipArchive).append(System.lineSeparator());
        }

        builder.append(System.lineSeparator()).append("Processed Files").append(System.lineSeparator());
        builder.append("----------------").append(System.lineSeparator());
        if (processedFiles.isEmpty()) {
            builder.append("No files were copied during this run.").append(System.lineSeparator());
        } else {
            for (BackupFile file : processedFiles) {
                builder.append(file.getRelativePath())
                        .append(" | ")
                        .append(file.getSize())
                        .append(" bytes | ")
                        .append(file.getLastModifiedTime())
                        .append(System.lineSeparator());
            }
        }

        if (!deletedFromMirror.isEmpty()) {
            builder.append(System.lineSeparator()).append("Removed From Mirror").append(System.lineSeparator());
            builder.append("-------------------").append(System.lineSeparator());
            for (Path path : deletedFromMirror) {
                builder.append(path).append(System.lineSeparator());
            }
        }

        return builder.toString();
    }

    public List<BackupFile> getProcessedFiles() {
        return Collections.unmodifiableList(processedFiles);
    }

    public int getSkippedFiles() {
        return skippedFiles;
    }

    public Path getZipArchive() {
        return zipArchive;
    }

    public int getCopiedFilesCount() {
        return processedFiles.size();
    }

    public long getCopiedBytes() {
        long total = 0L;
        for (BackupFile file : processedFiles) {
            total += file.getSize();
        }
        return total;
    }

    public Duration getDuration() {
        if (completedAt == null) {
            return Duration.ZERO;
        }
        return Duration.between(startedAt, completedAt);
    }
}
