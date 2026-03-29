package TimestampedLog;

public enum LogLevel {
    INFO("INFO"),
    WARNING("WARNING"),
    ERROR("ERROR"),
    DEBUG("DEBUG"),
    CRITICAL("CRITICAL");

    private String level;

    LogLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
}
