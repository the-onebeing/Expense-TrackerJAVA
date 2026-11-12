import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * ExpenseTrackerApp.java
 * The class that contains main(). It shows the menu, reads the user's
 * choice, and asks ExpenseManager to do the work. It handles all printing.
 */
public class ExpenseTrackerApp {

    private final ExpenseManager manager;

    public ExpenseTrackerApp() {
        // Storage is passed in (aggregation); FileStorage is one kind of Storage.
        this.manager = new ExpenseManager(new FileStorage());
    }

    public static void main(String[] args) {
        ExpenseTrackerApp app = new ExpenseTrackerApp();
        app.run();
    }

    // ---------- Main loop ----------

    private void run() {
        try {
            manager.loadData();
            System.out.println("Data loaded. Expenses on record: " + manager.getCount());
        } catch (IOException e) {
            System.out.println("Could not read the data file: " + e.getMessage());
        }

        try {
            boolean running = true;
            while (running) {
                printMenu();
                int choice = InputHelper.readInt("Enter your choice: ", 0, 9);
                running = handleChoice(choice);
            }
        } catch (NoSuchElementException e) {
            // Happens if the input stream is closed (e.g. Ctrl+D / Ctrl+Z).
            System.out.println("\nInput closed. Saving and exiting...");
            saveData();
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("        PERSONAL EXPENSE TRACKER");
        System.out.println("========================================");
        System.out.println(" 1. Add expense");
        System.out.println(" 2. View all expenses");
        System.out.println(" 3. Search expense by ID");
        System.out.println(" 4. Search expenses by category");
        System.out.println(" 5. Delete expense by ID");
        System.out.println(" 6. Show total expenses");
        System.out.println(" 7. Set monthly budget");
        System.out.println(" 8. View budget status");
        System.out.println(" 9. Expense summary");
        System.out.println(" 0. Save and exit");
        System.out.println("----------------------------------------");
    }

    /** Runs the chosen option. Returns false when the user wants to quit. */
    private boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                addExpense();
                break;
            case 2:
                viewAllExpenses();
                break;
            case 3:
                searchById();
                break;
            case 4:
                searchByCategory();
                break;
            case 5:
                deleteExpense();
                break;
            case 6:
                showTotal();
                break;
            case 7:
                setBudget();
                break;
            case 8:
                showBudgetStatus();
                break;
            case 9:
                showSummary();
                break;
            case 0:
                saveData();
                System.out.println("Data saved. Goodbye!");
                return false;
            default:
                System.out.println("Invalid choice.");
        }
        return true;
    }

    // ---------- Menu actions ----------

    private void addExpense() {
        System.out.println("\n--- Add Expense ---");
        System.out.println("(New expense ID will be " + Expense.getNextId() + ")");
        String description = InputHelper.readText("Description: ");
        double amount = InputHelper.readPositiveDouble("Amount (Rs): ");
        Category category = InputHelper.readCategory();
        LocalDate date = InputHelper.readDate("Date (yyyy-MM-dd, Enter for today): ");
        boolean recurring = InputHelper.readYesNo("Is this a recurring expense? (y/n): ");

        try {
            Expense added;
            if (recurring) {
                String frequency = InputHelper.readFrequency();
                added = new RecurringExpense(description, amount, category, date, frequency);
                manager.addExpense(added);                 // overload 1: takes an object
            } else {
                added = manager.addExpense(description, amount, category, date);   // overload 2
            }
            saveData();
            System.out.println("Expense added successfully with ID " + added.getId() + ".");
            warnIfOverBudget();
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add expense: " + e.getMessage());
        }
    }

    private void viewAllExpenses() {
        System.out.println("\n--- All Expenses ---");
        if (manager.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        printTable(manager.getAllExpenses());
    }

    private void searchById() {
        System.out.println("\n--- Search by ID ---");
        if (manager.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        int id = InputHelper.readInt("Enter expense ID: ", 1, Integer.MAX_VALUE);
        Expense found = manager.search(id);          // overload: int parameter
        if (found == null) {
            System.out.println("No expense found with ID " + id + ".");
        } else {
            System.out.println(Expense.tableHeader());
            System.out.println(found);
        }
    }

    private void searchByCategory() {
        System.out.println("\n--- Search by Category ---");
        Category category = InputHelper.readCategory();
        List<Expense> results = manager.search(category);   // overload: Category parameter
        if (results.isEmpty()) {
            System.out.println("No expenses found in category: " + category);
        } else {
            printTable(results);
        }
    }

    private void deleteExpense() {
        System.out.println("\n--- Delete Expense ---");
        if (manager.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        int id = InputHelper.readInt("Enter ID of the expense to delete: ", 1, Integer.MAX_VALUE);
        Expense found = manager.search(id);
        if (found == null) {
            System.out.println("No expense found with ID " + id + ".");
            return;
        }
        System.out.println(Expense.tableHeader());
        System.out.println(found);
        if (!InputHelper.readYesNo("Delete this expense? (y/n): ")) {
            System.out.println("Deletion cancelled.");
            return;
        }
        try {
            manager.deleteExpense(id);
            saveData();
            System.out.println("Expense " + id + " deleted.");
        } catch (ExpenseNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showTotal() {
        System.out.println("\n--- Total Expenses ---");
        System.out.println("Number of expenses : " + manager.getCount());
        System.out.println("Total spent        : " + money(manager.getTotal()));
        System.out.println("Spent this month   : " + money(manager.getCurrentMonthTotal()));
    }

    private void setBudget() {
        System.out.println("\n--- Set Monthly Budget ---");
        if (manager.getBudget().isSet()) {
            System.out.println("Current budget: " + money(manager.getBudget().getLimit()));
        }
        double amount = InputHelper.readPositiveDouble("Enter new monthly budget (Rs): ");
        manager.setMonthlyBudget(amount);
        saveData();
        System.out.println("Monthly budget set to " + money(amount) + ".");
        showBudgetStatus();
    }

    private void showBudgetStatus() {
        System.out.println("\n--- Budget Status ---");
        Budget budget = manager.getBudget();
        if (!budget.isSet()) {
            System.out.println("No monthly budget set. Use option 7 to set one.");
            return;
        }
        double spent = manager.getCurrentMonthTotal();
        String monthName = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy"));

        System.out.println("Month            : " + monthName);
        System.out.println("Monthly budget   : " + money(budget.getLimit()));
        System.out.println("Spent this month : " + money(spent));
        if (budget.isExceeded(spent)) {
            System.out.println("Remaining        : OVER BUDGET by " + money(-budget.getRemaining(spent)));
        } else {
            System.out.println("Remaining        : " + money(budget.getRemaining(spent)));
        }
        System.out.printf("Budget used      : %.1f%%%n", budget.getUsedPercent(spent));
    }

    private void showSummary() {
        System.out.println("\n--- Expense Summary ---");
        if (manager.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        double total = manager.getTotal();
        System.out.println("Total expenses recorded : " + manager.getCount());
        System.out.println("Total amount spent      : " + money(total));
        System.out.println("Average per expense     : " + money(total / manager.getCount()));

        System.out.println("\nCategory-wise spending:");
        for (Category c : Category.values()) {
            double categoryTotal = manager.getTotal(c);            // overload: Category parameter
            double percent = categoryTotal / total * 100;
            String bar = repeat("#", (int) Math.round(percent / 5)); // one # per 5%
            System.out.printf("  %-15s %12s  %5.1f%%  %s%n",
                    c.getLabel(), money(categoryTotal), percent, bar);
        }

        Expense highest = manager.getHighestExpense();
        System.out.println("\nHighest single expense:");
        System.out.println(Expense.tableHeader());
        System.out.println(highest);
    }

    // ---------- Helper methods ----------

    private void printTable(List<Expense> list) {
        String header = Expense.tableHeader();
        String line = repeat("-", header.length());
        double total = 0;

        System.out.println(line);
        System.out.println(header);
        System.out.println(line);
        for (Expense e : list) {
            System.out.println(e);   // toString() gives one formatted row
            total += e.getAmount();
        }
        System.out.println(line);
        System.out.println("Records: " + list.size() + "   Total: " + money(total));
    }

    private void warnIfOverBudget() {
        Budget budget = manager.getBudget();
        double spent = manager.getCurrentMonthTotal();
        if (budget.isExceeded(spent)) {
            System.out.println("WARNING: You have exceeded your monthly budget by "
                    + money(spent - budget.getLimit()) + "!");
        }
    }

    private void saveData() {
        try {
            manager.saveData();
        } catch (IOException e) {
            System.out.println("Error: could not save data - " + e.getMessage());
        }
    }

    // Repeats a text n times (works on every Java version).
    private String repeat(String text, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(text);
        }
        return sb.toString();
    }

    private String money(double amount) {
        return String.format("Rs. %.2f", amount);
    }
}
