@echo off
rem Rebuild ExpenseTracker.jar from the source files (needs the JDK, not just Java).
cd /d "%~dp0"
if exist build rmdir /s /q build
mkdir build
javac -d build src\*.java
if errorlevel 1 (
    echo Compilation failed.
    pause
    exit /b 1
)
echo Manifest-Version: 1.0> build\MANIFEST.MF
echo Main-Class: ExpenseTrackerApp>> build\MANIFEST.MF
jar cfm ExpenseTracker.jar build\MANIFEST.MF -C build .
rmdir /s /q build
echo Built ExpenseTracker.jar
pause
