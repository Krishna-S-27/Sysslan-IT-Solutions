# Personal File Analytics Utility

Task 4 for **Level 5 – Advanced Beginner / Professional Projects**.

This utility scans a folder of personal/work files, analyzes text-based content,
counts lines and words, aggregates frequent terms, and writes both a text report
and a CSV summary.

## Compile

```powershell
$files = Get-ChildItem .\src\com\fileanalytics\*.java | ForEach-Object { $_.FullName }
javac --release 8 -d .\out $files
```

## Run

```powershell
java -cp .\out com.fileanalytics.PersonalFileAnalyticsUtility .\utility-config.properties
```
