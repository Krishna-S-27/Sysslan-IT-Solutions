package FileOrganizer;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FileInfo {
    private String filename;
    private String fullPath;
    private long fileSize;
    private String fileExtension;
    private FileType fileType;
    private long lastModified;

    public FileInfo(File file) {
        this.filename = file.getName();
        this.fullPath = file.getAbsolutePath();
        this.fileSize = file.length();
        this.fileExtension = getExtension(filename);
        this.fileType = FileType.getFileType(filename);
        this.lastModified = file.lastModified();
    }

    private String getExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if(lastDot > 0) {
            return filename.substring(lastDot + 1).toLowerCase();
        }
        return "no extension";
    }

    public String getFilename() {
        return filename;
    }

    public String getFullPath() {
        return fullPath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getFormattedFileSize() {
        if(fileSize <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int unitIndex = (int)(Math.log10(fileSize) / Math.log10(1024));
        double size = fileSize / Math.pow(1024, unitIndex);
        return String.format("%.2f %s", size, units[unitIndex]);
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public FileType getFileType() {
        return fileType;
    }

    public String getLastModifiedDate() {
        Instant instant = Instant.ofEpochMilli(lastModified);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

    public String toString() {
        return "Filename: " + filename + "\nPath: " + fullPath + "\nSize: " + getFormattedFileSize() +
                "\nExtension: " + fileExtension + "\nType: " + fileType.getFolderName() +
                "\nLast Modified: " + getLastModifiedDate();
    }
}