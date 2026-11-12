# Personal Expense Tracker

A console-based expense tracker written in Java using Object-Oriented Programming.
It also includes a browser version (in `docs/`) that can be hosted free on GitHub Pages.

## Features
- Add expenses (ID, description, amount, category, date), including recurring ones
- View all expenses in a formatted table
- Categories: Food, Travel, Shopping, Education, Entertainment, Others
- Search by ID or category
- Delete by ID
- Total expenses
- Monthly budget with remaining balance and over-budget warning
- Summary: category-wise spending and highest expense
- Data saved to `expenses.txt`, so it persists after closing

## How to run (Java version)
You only need **Java 8 or higher** installed (https://adoptium.net).

- **Windows:** double-click `run.bat`
- **Mac / Linux:** run `./run.sh`
- **Any system:** `java -jar ExpenseTracker.jar`

To rebuild the jar after changing the code (needs the JDK): run `build.bat` (Windows) or `./build.sh` (Mac/Linux).

## Project structure
```
src/
  ExpenseTrackerApp.java        main(), menu and printing
  ExpenseManager.java           logic: add, search, delete, totals, budget
  Transaction.java              abstract parent class
  Expense.java                  extends Transaction
  RecurringExpense.java         extends Expense
  Category.java                 enum of categories
  Budget.java                   monthly budget calculations
  FileStorage.java              saves/loads expenses.txt
  InputHelper.java              validated keyboard input
  Recordable.java, Storage.java interfaces
  ExpenseNotFoundException.java custom exception
docs/index.html                 browser version (GitHub Pages)
```

## OOP concepts used
Classes and objects, encapsulation, abstraction (abstract class and interfaces), inheritance
(`Transaction` -> `Expense` -> `RecurringExpense`), method overloading, method overriding,
constructors, static variables and methods, access modifiers, composition and aggregation.

## Browser version
Enable **Settings -> Pages -> Deploy from a branch -> `main` / `/docs`** on GitHub.
The site will be available at `https://<your-username>.github.io/<repo-name>/`.
