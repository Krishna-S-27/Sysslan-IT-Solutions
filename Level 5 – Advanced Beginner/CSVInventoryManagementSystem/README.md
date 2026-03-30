# CSV Inventory Management System

Task 3 for **Level 5 – Advanced Beginner / Professional Projects**.

This project manages product inventory from a CSV file, updates stock and pricing,
persists data back to CSV, and generates a report with category summaries and low-stock alerts.

## Compile

```powershell
$files = Get-ChildItem .\src\com\inventory\*.java | ForEach-Object { $_.FullName }
javac --release 8 -d .\out $files
```

## Run

```powershell
java -cp .\out com.inventory.CSVInventoryManagementSystem .\sample-data\inventory.csv .\sample-data\inventory-report.txt
```
