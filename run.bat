@echo off
rem Run the Expense Tracker on Windows. Double-click this file.
cd /d "%~dp0"
where java >nul 2>nul
if errorlevel 1 (
    echo Java was not found. Install Java 8 or higher from https://adoptium.net and try again.
    pause
    exit /b 1
)
java -jar ExpenseTracker.jar
pause
