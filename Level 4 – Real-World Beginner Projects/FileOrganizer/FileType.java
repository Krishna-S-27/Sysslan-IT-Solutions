package FileOrganizer;

public enum FileType {
    IMAGE("Images", new String[]{".jpg", ".jpeg", ".png", ".gif", ".bmp", ".svg", ".ico", ".webp"}),
    DOCUMENT("Documents", new String[]{".pdf", ".doc", ".docx", ".txt", ".xlsx", ".xls", ".ppt", ".pptx"}),
    VIDEO("Videos", new String[]{".mp4", ".avi", ".mkv", ".mov", ".wmv", ".flv", ".webm"}),
    AUDIO("Audio", new String[]{".mp3", ".wav", ".flac", ".aac", ".wma", ".m4a", ".ogg"}),
    ARCHIVE("Archives", new String[]{".zip", ".rar", ".7z", ".tar", ".gz", ".bz2", ".iso"}),
    CODE("Code", new String[]{".java", ".py", ".cpp", ".c", ".js", ".html", ".css", ".xml", ".json"}),
    EXECUTABLE("Executables", new String[]{".exe", ".bat", ".sh", ".com", ".dll", ".app"}),
    OTHER("Others", new String[]{});

    private String folderName;
    private String[] extensions;

    FileType(String folderName, String[] extensions) {
        this.folderName = folderName;
        this.extensions = extensions;
    }

    public String getFolderName() {
        return folderName;
    }

    public String[] getExtensions() {
        return extensions;
    }

    public static FileType getFileType(String filename) {
        String extension = getFileExtension(filename).toLowerCase();

        for(FileType type : FileType.values()) {
            if(type == FileType.OTHER) continue;

            for(String ext : type.extensions) {
                if(ext.equalsIgnoreCase(extension)) {
                    return type;
                }
            }
        }

        return FileType.OTHER;
    }

    private static String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if(lastDot > 0) {
            return filename.substring(lastDot);
        }
        return "";
    }
}
