# Level 5 - Advanced Beginner / Professional Projects

This folder contains Level 5 mini real-world Java applications. These projects combine multiple classes, file handling, reporting, configuration, and more professional project structure.

## Task 1: Web Scraper
- Folder: `WebScraper`
- Main class: `com.webscraper.WebScraper`
- Purpose: Uses JSoup to fetch and extract page content, links, images, headings, and metadata.
- Notes: This project includes a `pom.xml` and follows a Maven-style structure.

## Task 2: Automated Folder Backup Tool
- Folder: `AutomatedFolderBackupTool`
- Main class: `com.folderbackup.FolderBackupTool`
- Purpose: Creates full or incremental backups, maintains a mirror copy, generates ZIP archives, and writes backup reports.

## Task 3: CSV Inventory Management System
- Folder: `CSVInventoryManagementSystem`
- Main class: `com.inventory.CSVInventoryManagementSystem`
- Purpose: Loads product data from CSV, updates inventory, saves changes, and generates inventory reports.

## Task 4: Personal File Analytics Utility
- Folder: `PersonalFileAnalyticsUtility`
- Main class: `com.fileanalytics.PersonalFileAnalyticsUtility`
- Purpose: Scans text-based files, analyzes file content, counts words and extensions, and writes report outputs.

## How To Run

### Task 1: Web Scraper

Use Maven from the `WebScraper` folder:

```powershell
mvn compile
mvn exec:java
```

### Task 2: Automated Folder Backup Tool

```powershell
cd ".\AutomatedFolderBackupTool"
$files = Get-ChildItem .\src\com\folderbackup\*.java | ForEach-Object { $_.FullName }
javac --release 8 -d .\out $files
java -cp .\out com.folderbackup.FolderBackupTool .\backup-config.properties
```

### Task 3: CSV Inventory Management System

```powershell
cd ".\CSVInventoryManagementSystem"
$files = Get-ChildItem .\src\com\inventory\*.java | ForEach-Object { $_.FullName }
javac --release 8 -d .\out $files
java -cp .\out com.inventory.CSVInventoryManagementSystem .\sample-data\inventory.csv .\sample-data\inventory-report.txt
```

### Task 4: Personal File Analytics Utility

```powershell
cd ".\PersonalFileAnalyticsUtility"
$files = Get-ChildItem .\src\com\fileanalytics\*.java | ForEach-Object { $_.FullName }
javac --release 8 -d .\out $files
java -cp .\out com.fileanalytics.PersonalFileAnalyticsUtility .\utility-config.properties
```
