#!/bin/sh
# Run the Expense Tracker on Linux / macOS.
cd "$(dirname "$0")" || exit 1
if ! command -v java >/dev/null 2>&1; then
    echo "Java was not found. Install Java 8 or higher from https://adoptium.net and try again."
    exit 1
fi
java -jar ExpenseTracker.jar
