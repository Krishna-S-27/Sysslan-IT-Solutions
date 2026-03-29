package TimestampedLog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogEntry {
    private LocalDateTime timestamp;
    private LogLevel level;
    private String message;
    private String source;

    public LogEntry(LogLevel level, String message, String source) {
        this.timestamp = LocalDateTime.now();
        this.level = level;
        this.message = message;
        this.source = source;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getSource() {
        return source;
    }

    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return timestamp.format(formatter);
    }

    public String toString() {
        return String.format("[%s] [%s] [%s] %s",
                getFormattedTimestamp(), level.getLevel(), source, message);
    }
}
