# Automated Folder Backup Tool

Task 2 for **Level 5 – Advanced Beginner / Professional Projects**.

This Java project backs up one folder into timestamped snapshots, keeps a `latest_backup`
mirror for incremental comparisons, optionally creates a ZIP archive, and writes a
human-readable report after each run.

## Features

- Full and incremental backups
- Timestamped snapshot folders
- `latest_backup` mirror for change detection
- ZIP archive generation for each snapshot
- Backup summary report with copied, skipped, and removed files
- Relative or absolute paths supported through a properties config file

## Project Structure

```text
AutomatedFolderBackupTool/
|-- backup-config.properties
|-- sample-data/
|-- src/com/folderbackup/
`-- README.md
```

## Compile

```powershell
javac --release 8 -d out src\com\folderbackup\*.java
```

## Run

```powershell
java -cp out com.folderbackup.FolderBackupTool backup-config.properties
```

## Configuration

Edit `backup-config.properties`:

```properties
source.directory=sample-data/source
backup.directory=sample-data/backups
backup.type=INCREMENTAL
create.zip=true
report.file=sample-data/backup-report.txt
```

`backup.type` supports `FULL` and `INCREMENTAL`.
