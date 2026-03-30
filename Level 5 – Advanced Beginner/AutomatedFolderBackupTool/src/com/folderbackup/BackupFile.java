package com.folderbackup;

import java.nio.file.Path;
import java.time.Instant;

public class BackupFile {
    private final Path sourcePath;
    private final Path relativePath;
    private final long size;
    private final Instant lastModifiedTime;
    private final boolean copied;

    public BackupFile(Path sourcePath, Path relativePath, long size, Instant lastModifiedTime, boolean copied) {
        this.sourcePath = sourcePath;
        this.relativePath = relativePath;
        this.size = size;
        this.lastModifiedTime = lastModifiedTime;
        this.copied = copied;
    }

    public Path getSourcePath() {
        return sourcePath;
    }

    public Path getRelativePath() {
        return relativePath;
    }

    public long getSize() {
        return size;
    }

    public Instant getLastModifiedTime() {
        return lastModifiedTime;
    }

    public boolean isCopied() {
        return copied;
    }
}
