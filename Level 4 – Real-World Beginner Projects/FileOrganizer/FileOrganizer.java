package FileOrganizer;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileOrganizer {
    private String sourceDirectory;
    private String organizationStrategy;
    private int filesOrganized;
    private int filesFailed;
    private ArrayList<FileInfo> processedFiles;

    public FileOrganizer(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
        this.organizationStrategy = "type";
        this.filesOrganized = 0;
        this.filesFailed = 0;
        this.processedFiles = new ArrayList<>();
    }

    public void setOrganizationStrategy(String strategy) {
        this.organizationStrategy = strategy.toLowerCase();
    }

    public boolean validateDirectory() {
        File dir = new File(sourceDirectory);

        if(!dir.exists()) {
            System.out.println("Error: Directory does not exist!");
            return false;
        }

        if(!dir.isDirectory()) {
            System.out.println("Error: Path is not a directory!");
            return false;
        }

        if(!dir.canRead()) {
            System.out.println("Error: No read permission for directory!");
            return false;
        }

        return true;
    }

    public void organizeFiles() {
        if(!validateDirectory()) {
            return;
        }

        File directory = new File(sourceDirectory);
        File[] files = directory.listFiles();

        if(files == null || files.length == 0) {
            System.out.println("Directory is empty!");
            return;
        }

        System.out.println("Starting file organization...\n");

        for(File file : files) {
            if(file.isFile()) {
                FileInfo fileInfo = new FileInfo(file);
                processedFiles.add(fileInfo);

                if(organizeFile(file, fileInfo)) {
                    filesOrganized++;
                } else {
                    filesFailed++;
                }
            }
        }

        displayOrganizationSummary();
    }

    private boolean organizeFile(File file, FileInfo fileInfo) {
        try {
            String targetFolderName = getTargetFolderName(fileInfo);
            File targetFolder = new File(sourceDirectory, targetFolderName);

            if(!targetFolder.exists()) {
                targetFolder.mkdir();
            }

            File targetFile = new File(targetFolder, file.getName());

            if(targetFile.exists()) {
                String newFilename = getUniqueFilename(file.getName(), targetFolder);
                targetFile = new File(targetFolder, newFilename);
            }

            Files.move(file.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Moved: " + file.getName() + " -> " + targetFolderName);

            return true;
        } catch(IOException e) {
            System.out.println("Failed to move: " + file.getName() + " - " + e.getMessage());
            return false;
        }
    }

    private String getTargetFolderName(FileInfo fileInfo) {
        switch(organizationStrategy) {
            case "type":
                return fileInfo.getFileType().getFolderName();
            case "extension":
                return fileInfo.getFileExtension().isEmpty() ? "no_extension" : fileInfo.getFileExtension();
            case "size":
                return getSizeCategory(fileInfo.getFileSize());
            case "date":
                return getDateCategory(fileInfo.getLastModifiedDate());
            default:
                return fileInfo.getFileType().getFolderName();
        }
    }

    private String getSizeCategory(long fileSize) {
        if(fileSize < 1024 * 100) {
            return "Small (< 100KB)";
        } else if(fileSize < 1024 * 1024 * 10) {
            return "Medium (100KB - 10MB)";
        } else if(fileSize < 1024 * 1024 * 100) {
            return "Large (10MB - 100MB)";
        } else {
            return "Very Large (> 100MB)";
        }
    }

    private String getDateCategory(String lastModified) {
        String date = lastModified.substring(0, 7);
        return "Organized_" + date;
    }

    private String getUniqueFilename(String originalName, File folder) {
        String name = originalName;
        int counter = 1;

        int lastDot = originalName.lastIndexOf('.');
        String baseName = lastDot > 0 ? originalName.substring(0, lastDot) : originalName;
        String extension = lastDot > 0 ? originalName.substring(lastDot) : "";

        while(new File(folder, name).exists()) {
            name = baseName + "(" + counter + ")" + extension;
            counter++;
        }

        return name;
    }

    public void displayOrganizationSummary() {
        System.out.println("\n========== Organization Summary ==========");
        System.out.println("Files Organized: " + filesOrganized);
        System.out.println("Files Failed: " + filesFailed);
        System.out.println("Total Files: " + processedFiles.size());
        System.out.println("Strategy Used: " + organizationStrategy);
        System.out.println("==========================================\n");
    }

    public void getStatistics() {
        if(processedFiles.isEmpty()) {
            System.out.println("No files processed!");
            return;
        }

        Map<FileType, Integer> typeCount = new HashMap<>();
        Map<String, Long> extensionSize = new HashMap<>();
        long totalSize = 0;

        for(FileInfo file : processedFiles) {
            typeCount.put(file.getFileType(), typeCount.getOrDefault(file.getFileType(), 0) + 1);
            extensionSize.put(file.getFileExtension(),
                    extensionSize.getOrDefault(file.getFileExtension(), 0L) + file.getFileSize());
            totalSize += file.getFileSize();
        }

        System.out.println("\n========== File Statistics ==========");
        System.out.println("Total Files: " + processedFiles.size());
        System.out.println("Total Size: " + formatBytes(totalSize));

        System.out.println("\nBy Type:");
        for(FileType type : FileType.values()) {
            int count = typeCount.getOrDefault(type, 0);
            if(count > 0) {
                System.out.println("  " + type.getFolderName() + ": " + count + " files");
            }
        }

        System.out.println("\nBy Extension:");
        for(String ext : extensionSize.keySet()) {
            long size = extensionSize.get(ext);
            System.out.println("  ." + ext + ": " + formatBytes(size));
        }
        System.out.println("=====================================\n");
    }

    private String formatBytes(long bytes) {
        if(bytes <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB"};
        int unitIndex = (int)(Math.log10(bytes) / Math.log10(1024));
        double size = bytes / Math.pow(1024, unitIndex);
        return String.format("%.2f %s", size, units[unitIndex]);
    }

    public void listFiles() {
        if(processedFiles.isEmpty()) {
            System.out.println("No files found!");
            return;
        }

        System.out.println("\n========== Files in Directory ==========");
        System.out.println(String.format("%-30s | %-10s | %-15s | %-15s",
                "Filename", "Extension", "Size", "Type"));
        System.out.println("-----------------------------------------------------------");

        for(FileInfo file : processedFiles) {
            System.out.println(String.format("%-30s | %-10s | %-15s | %-15s",
                    file.getFilename(),
                    file.getFileExtension(),
                    file.getFormattedFileSize(),
                    file.getFileType().getFolderName()));
        }
        System.out.println("=========================================\n");
    }

    public void filterByType(FileType type) {
        ArrayList<FileInfo> filtered = new ArrayList<>();
        for(FileInfo file : processedFiles) {
            if(file.getFileType() == type) {
                filtered.add(file);
            }
        }

        if(filtered.isEmpty()) {
            System.out.println("No files of type: " + type.getFolderName());
            return;
        }

        System.out.println("\n========== Files of Type: " + type.getFolderName() + " ==========");
        for(FileInfo file : filtered) {
            System.out.println(file);
            System.out.println("---");
        }
        System.out.println("================================\n");
    }

    public void displayMenu() {
        System.out.println("\n========== File Organizer ==========");
        System.out.println("1. Organize Files by Type");
        System.out.println("2. Organize Files by Extension");
        System.out.println("3. Organize Files by Size");
        System.out.println("4. Organize Files by Date");
        System.out.println("5. View File Statistics");
        System.out.println("6. List All Files");
        System.out.println("7. Filter Files by Type");
        System.out.println("8. Change Directory");
        System.out.println("9. Exit");
        System.out.print("Enter choice: ");
    }

    private int getValidChoice() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if(choice >= 1 && choice <= 9) {
                    return choice;
                } else {
                    System.out.print("Enter a number between 1 and 9: ");
                }
            } catch(NumberFormatException e) {
                System.out.print("Invalid input! Enter a number: ");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Automated File Organizer\n");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter directory path to organize: ");
        String dirPath = sc.nextLine();

        if(dirPath.isEmpty()) {
            dirPath = ".";
        }

        FileOrganizer organizer = new FileOrganizer(dirPath);

        if(!organizer.validateDirectory()) {
            System.out.println("Cannot proceed with invalid directory!");
            return;
        }

        organizer.processedFiles.clear();
        File directory = new File(dirPath);
        File[] files = directory.listFiles();

        if(files != null) {
            for(File file : files) {
                if(file.isFile()) {
                    organizer.processedFiles.add(new FileInfo(file));
                }
            }
        }

        boolean running = true;
        while(running) {
            organizer.displayMenu();
            int choice = organizer.getValidChoice();

            switch(choice) {
                case 1:
                    organizer.setOrganizationStrategy("type");
                    organizer.organizeFiles();
                    break;

                case 2:
                    organizer.setOrganizationStrategy("extension");
                    organizer.organizeFiles();
                    break;

                case 3:
                    organizer.setOrganizationStrategy("size");
                    organizer.organizeFiles();
                    break;

                case 4:
                    organizer.setOrganizationStrategy("date");
                    organizer.organizeFiles();
                    break;

                case 5:
                    organizer.getStatistics();
                    break;

                case 6:
                    organizer.listFiles();
                    break;

                case 7:
                    System.out.println("Select File Type:");
                    FileType[] types = FileType.values();
                    for(int i = 0; i < types.length; i++) {
                        System.out.println((i + 1) + ". " + types[i].getFolderName());
                    }
                    System.out.print("Enter choice: ");
                    int typeChoice = Integer.parseInt(sc.nextLine());

                    if(typeChoice >= 1 && typeChoice <= types.length) {
                        organizer.filterByType(types[typeChoice - 1]);
                    } else {
                        System.out.println("Invalid choice!");
                    }
                    break;

                case 8:
                    System.out.print("Enter new directory path: ");
                    String newPath = sc.nextLine();
                    organizer = new FileOrganizer(newPath);

                    if(!organizer.validateDirectory()) {
                        System.out.println("Invalid directory!");
                    } else {
                        organizer.processedFiles.clear();
                        File newDir = new File(newPath);
                        File[] newFiles = newDir.listFiles();

                        if(newFiles != null) {
                            for(File file : newFiles) {
                                if(file.isFile()) {
                                    organizer.processedFiles.add(new FileInfo(file));
                                }
                            }
                        }
                        System.out.println("Directory changed to: " + newPath);
                    }
                    break;

                case 9:
                    System.out.println("Thank you for using File Organizer!");
                    running = false;
                    break;
            }
        }

        sc.close();
    }
}
