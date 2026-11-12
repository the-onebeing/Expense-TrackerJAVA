#!/bin/sh
# Rebuild ExpenseTracker.jar from the source files (needs the JDK, not just Java).
cd "$(dirname "$0")" || exit 1
rm -rf build && mkdir build
javac -d build src/*.java || exit 1
printf 'Manifest-Version: 1.0\nMain-Class: ExpenseTrackerApp\n' > build/MANIFEST.MF
jar cfm ExpenseTracker.jar build/MANIFEST.MF -C build .
rm -rf build
echo "Built ExpenseTracker.jar"
