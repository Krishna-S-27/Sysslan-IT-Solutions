package TimestampedLog;

import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private ArrayList<LogEntry> logs;
    private String logFilename;
    private boolean consoleOutput;
    private LogLevel minimumLevel;

    public Logger(String filename) {
        this.logs = new ArrayList<>();
        this.logFilename = filename;
        this.consoleOutput = true;
        this.minimumLevel = LogLevel.DEBUG;
        loadLogs();
    }

    public Logger(String filename, boolean consoleOutput) {
        this.logs = new ArrayList<>();
        this.logFilename = filename;
        this.consoleOutput = consoleOutput;
        this.minimumLevel = LogLevel.DEBUG;
        loadLogs();
    }

    public void setMinimumLevel(LogLevel level) {
        this.minimumLevel = level;
    }

    public void setConsoleOutput(boolean enable) {
        this.consoleOutput = enable;
    }

    private boolean shouldLog(LogLevel level) {
        int logLevelValue = getLogLevelValue(level);
        int minLevelValue = getLogLevelValue(minimumLevel);
        return logLevelValue >= minLevelValue;
    }

    private int getLogLevelValue(LogLevel level) {
        switch(level) {
            case DEBUG:
                return 0;
            case INFO:
                return 1;
            case WARNING:
                return 2;
            case ERROR:
                return 3;
            case CRITICAL:
                return 4;
            default:
                return 1;
        }
    }

    public void info(String message, String source) {
        if(shouldLog(LogLevel.INFO)) {
            LogEntry entry = new LogEntry(LogLevel.INFO, message, source);
            logs.add(entry);
            if(consoleOutput) {
                System.out.println(entry);
            }
            saveLogs();
        }
    }

    public void warning(String message, String source) {
        if(shouldLog(LogLevel.WARNING)) {
            LogEntry entry = new LogEntry(LogLevel.WARNING, message, source);
            logs.add(entry);
            if(consoleOutput) {
                System.out.println(entry);
            }
            saveLogs();
        }
    }

    public void error(String message, String source) {
        if(shouldLog(LogLevel.ERROR)) {
            LogEntry entry = new LogEntry(LogLevel.ERROR, message, source);
            logs.add(entry);
            if(consoleOutput) {
                System.out.println(entry);
            }
            saveLogs();
        }
    }

    public void debug(String message, String source) {
        if(shouldLog(LogLevel.DEBUG)) {
            LogEntry entry = new LogEntry(LogLevel.DEBUG, message, source);
            logs.add(entry);
            if(consoleOutput) {
                System.out.println(entry);
            }
            saveLogs();
        }
    }

    public void critical(String message, String source) {
        if(shouldLog(LogLevel.CRITICAL)) {
            LogEntry entry = new LogEntry(LogLevel.CRITICAL, message, source);
            logs.add(entry);
            if(consoleOutput) {
                System.out.println(entry);
            }
            saveLogs();
        }
    }

    public void displayAllLogs() {
        if(logs.isEmpty()) {
            System.out.println("No logs available!");
            return;
        }

        System.out.println("\n========== All Logs ==========");
        for(LogEntry entry : logs) {
            System.out.println(entry);
        }
        System.out.println("==============================\n");
    }

    public void displayLogsByLevel(LogLevel level) {
        ArrayList<LogEntry> filtered = new ArrayList<>();
        for(LogEntry entry : logs) {
            if(entry.getLevel() == level) {
                filtered.add(entry);
            }
        }

        if(filtered.isEmpty()) {
            System.out.println("No logs found for level: " + level.getLevel());
            return;
        }

        System.out.println("\n========== Logs for " + level.getLevel() + " ==========");
        for(LogEntry entry : filtered) {
            System.out.println(entry);
        }
        System.out.println("===================================\n");
    }

    public void displayLogsBySource(String source) {
        ArrayList<LogEntry> filtered = new ArrayList<>();
        for(LogEntry entry : logs) {
            if(entry.getSource().equalsIgnoreCase(source)) {
                filtered.add(entry);
            }
        }

        if(filtered.isEmpty()) {
            System.out.println("No logs found from source: " + source);
            return;
        }

        System.out.println("\n========== Logs from " + source + " ==========");
        for(LogEntry entry : filtered) {
            System.out.println(entry);
        }
        System.out.println("===================================\n");
    }

    public void displayLogsByDateRange(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        ArrayList<LogEntry> filtered = new ArrayList<>();
        for(LogEntry entry : logs) {
            LocalDateTime entryDate = entry.getTimestamp();
            String entryDateStr = entryDate.format(formatter);

            if(entryDateStr.compareTo(startDate) >= 0 && entryDateStr.compareTo(endDate) <= 0) {
                filtered.add(entry);
            }
        }

        if(filtered.isEmpty()) {
            System.out.println("No logs found in date range: " + startDate + " to " + endDate);
            return;
        }

        System.out.println("\n========== Logs from " + startDate + " to " + endDate + " ==========");
        for(LogEntry entry : filtered) {
            System.out.println(entry);
        }
        System.out.println("===================================\n");
    }

    public void getLogStatistics() {
        if(logs.isEmpty()) {
            System.out.println("No logs available!");
            return;
        }

        Map<LogLevel, Integer> levelCount = new HashMap<>();
        Map<String, Integer> sourceCount = new HashMap<>();

        for(LogEntry entry : logs) {
            levelCount.put(entry.getLevel(), levelCount.getOrDefault(entry.getLevel(), 0) + 1);
            sourceCount.put(entry.getSource(), sourceCount.getOrDefault(entry.getSource(), 0) + 1);
        }

        System.out.println("\n========== Log Statistics ==========");
        System.out.println("Total Logs: " + logs.size());

        System.out.println("\nBy Level:");
        for(LogLevel level : LogLevel.values()) {
            int count = levelCount.getOrDefault(level, 0);
            System.out.println("  " + level.getLevel() + ": " + count);
        }

        System.out.println("\nBy Source:");
        for(String source : sourceCount.keySet()) {
            System.out.println("  " + source + ": " + sourceCount.get(source));
        }

        System.out.println("====================================\n");
    }

    public void clearLogs() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Are you sure you want to clear all logs? (y/n): ");
        String response = sc.nextLine();

        if(response.equalsIgnoreCase("y")) {
            logs.clear();
            saveLogs();
            System.out.println("All logs cleared!");
        } else {
            System.out.println("Operation cancelled!");
        }
    }

    public void exportLogs(String exportFilename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(exportFilename));

            writer.write("========== Log Export ==========\n");
            writer.write("Total Logs: " + logs.size() + "\n");
            writer.write("Export Date: " + LocalDateTime.now() + "\n");
            writer.write("===============================\n\n");

            for(LogEntry entry : logs) {
                writer.write(entry.toString());
                writer.newLine();
            }

            writer.close();
            System.out.println("Logs exported to " + exportFilename);
        } catch(IOException e) {
            System.out.println("Error exporting logs: " + e.getMessage());
        }
    }

    public int getTotalLogs() {
        return logs.size();
    }

    private void saveLogs() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFilename));

            for(LogEntry entry : logs) {
                writer.write(entry.getFormattedTimestamp() + "|" + entry.getLevel().getLevel() + "|" +
                        entry.getSource() + "|" + entry.getMessage());
                writer.newLine();
            }

            writer.close();
        } catch(IOException e) {
            System.out.println("Error saving logs: " + e.getMessage());
        }
    }

    private void loadLogs() {
        try {
            File file = new File(logFilename);
            if(!file.exists()) {
                System.out.println("New log file will be created: " + logFilename + "\n");
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(logFilename));
            String line;
            int count = 0;

            while((line = reader.readLine()) != null) {
                if(line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                if(parts.length == 4) {
                    String timestamp = parts[0].trim();
                    String level = parts[1].trim();
                    String source = parts[2].trim();
                    String message = parts[3].trim();

                    LogLevel logLevel = LogLevel.valueOf(level);
                    LogEntry entry = new LogEntry(logLevel, message, source);
                    logs.add(entry);
                    count++;
                }
            }

            reader.close();
            System.out.println("Loaded " + count + " log entries from " + logFilename + "\n");
        } catch(IOException e) {
            System.out.println("Error loading logs: " + e.getMessage());
        }
    }

    public void displayMenu() {
        System.out.println("\n========== Log File Generator ==========");
        System.out.println("1. Create Log Entry (INFO)");
        System.out.println("2. Create Log Entry (WARNING)");
        System.out.println("3. Create Log Entry (ERROR)");
        System.out.println("4. Create Log Entry (DEBUG)");
        System.out.println("5. Create Log Entry (CRITICAL)");
        System.out.println("6. View All Logs");
        System.out.println("7. Filter Logs by Level");
        System.out.println("8. Filter Logs by Source");
        System.out.println("9. Filter Logs by Date Range");
        System.out.println("10. Log Statistics");
        System.out.println("11. Total Logs");
        System.out.println("12. Export Logs");
        System.out.println("13. Clear Logs");
        System.out.println("14. Change Minimum Log Level");
        System.out.println("15. Exit");
        System.out.print("Enter choice: ");
    }

    private int getValidChoice() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if(choice >= 1 && choice <= 15) {
                    return choice;
                } else {
                    System.out.print("Enter a number between 1 and 15: ");
                }
            } catch(NumberFormatException e) {
                System.out.print("Invalid input! Enter a number: ");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Timestamped Log File Generator\n");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter log filename (default: application.log): ");
        String filename = sc.nextLine();

        if(filename.isEmpty()) {
            filename = "application.log";
        }

        if(!filename.endsWith(".log")) {
            filename += ".log";
        }

        Logger logger = new Logger(filename);

        boolean running = true;
        while(running) {
            logger.displayMenu();
            int choice = logger.getValidChoice();

            switch(choice) {
                case 1:
                    System.out.print("Enter message: ");
                    String infoMsg = sc.nextLine();
                    System.out.print("Enter source: ");
                    String infoSource = sc.nextLine();
                    logger.info(infoMsg, infoSource);
                    break;

                case 2:
                    System.out.print("Enter message: ");
                    String warnMsg = sc.nextLine();
                    System.out.print("Enter source: ");
                    String warnSource = sc.nextLine();
                    logger.warning(warnMsg, warnSource);
                    break;

                case 3:
                    System.out.print("Enter message: ");
                    String errMsg = sc.nextLine();
                    System.out.print("Enter source: ");
                    String errSource = sc.nextLine();
                    logger.error(errMsg, errSource);
                    break;

                case 4:
                    System.out.print("Enter message: ");
                    String dbgMsg = sc.nextLine();
                    System.out.print("Enter source: ");
                    String dbgSource = sc.nextLine();
                    logger.debug(dbgMsg, dbgSource);
                    break;

                case 5:
                    System.out.print("Enter message: ");
                    String critMsg = sc.nextLine();
                    System.out.print("Enter source: ");
                    String critSource = sc.nextLine();
                    logger.critical(critMsg, critSource);
                    break;

                case 6:
                    logger.displayAllLogs();
                    break;

                case 7:
                    System.out.println("Select Level: 1=DEBUG, 2=INFO, 3=WARNING, 4=ERROR, 5=CRITICAL");
                    System.out.print("Enter choice: ");
                    int levelChoice = Integer.parseInt(sc.nextLine());

                    LogLevel[] levels = {LogLevel.DEBUG, LogLevel.INFO, LogLevel.WARNING, LogLevel.ERROR, LogLevel.CRITICAL};
                    if(levelChoice >= 1 && levelChoice <= 5) {
                        logger.displayLogsByLevel(levels[levelChoice - 1]);
                    } else {
                        System.out.println("Invalid choice!");
                    }
                    break;

                case 8:
                    System.out.print("Enter source to filter: ");
                    String filterSource = sc.nextLine();
                    logger.displayLogsBySource(filterSource);
                    break;

                case 9:
                    System.out.print("Enter start date (yyyy-MM-dd): ");
                    String startDate = sc.nextLine();
                    System.out.print("Enter end date (yyyy-MM-dd): ");
                    String endDate = sc.nextLine();
                    logger.displayLogsByDateRange(startDate, endDate);
                    break;

                case 10:
                    logger.getLogStatistics();
                    break;

                case 11:
                    System.out.println("Total logs: " + logger.getTotalLogs());
                    break;

                case 12:
                    System.out.print("Enter export filename: ");
                    String exportFile = sc.nextLine();
                    logger.exportLogs(exportFile);
                    break;

                case 13:
                    logger.clearLogs();
                    break;

                case 14:
                    System.out.println("Select Minimum Level: 1=DEBUG, 2=INFO, 3=WARNING, 4=ERROR, 5=CRITICAL");
                    System.out.print("Enter choice: ");
                    int minChoice = Integer.parseInt(sc.nextLine());

                    LogLevel[] minLevels = {LogLevel.DEBUG, LogLevel.INFO, LogLevel.WARNING, LogLevel.ERROR, LogLevel.CRITICAL};
                    if(minChoice >= 1 && minChoice <= 5) {
                        logger.setMinimumLevel(minLevels[minChoice - 1]);
                        System.out.println("Minimum log level set to: " + minLevels[minChoice - 1].getLevel());
                    } else {
                        System.out.println("Invalid choice!");
                    }
                    break;

                case 15:
                    System.out.println("Thank you for using Log File Generator!");
                    running = false;
                    break;
            }
        }

        sc.close();
    }
}
