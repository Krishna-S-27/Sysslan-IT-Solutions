package com.fileanalytics;

import java.nio.file.Path;

public class FileSummary {
    private final Path relativePath;
    private final String extension;
    private final long bytes;
    private final int lines;
    private final int words;
    private final int characters;

    public FileSummary(Path relativePath, String extension, long bytes, int lines, int words, int characters) {
        this.relativePath = relativePath;
        this.extension = extension;
        this.bytes = bytes;
        this.lines = lines;
        this.words = words;
        this.characters = characters;
    }

    public Path getRelativePath() {
        return relativePath;
    }

    public String getExtension() {
        return extension;
    }

    public long getBytes() {
        return bytes;
    }

    public int getLines() {
        return lines;
    }

    public int getWords() {
        return words;
    }

    public int getCharacters() {
        return characters;
    }
}
