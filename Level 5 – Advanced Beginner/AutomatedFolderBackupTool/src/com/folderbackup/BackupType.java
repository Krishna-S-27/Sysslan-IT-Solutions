package com.folderbackup;

public enum BackupType {
    FULL,
    INCREMENTAL;

    public static BackupType from(String value) {
        if (value == null) {
            return INCREMENTAL;
        }

        for (BackupType type : values()) {
            if (type.name().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unsupported backup type: " + value);
    }
}
