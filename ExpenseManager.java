import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * ExpenseManager.java
 * The "brain" of the application. It owns the list of expenses and the
 * budget and performs every operation (add, search, delete, totals ...).
 * It never prints anything or reads keyboard input - that is the job of
 * ExpenseTrackerApp. This separation keeps the code easy to understand.
 *
 * COMPOSITION: the manager creates and owns the ArrayList and the Budget;
 *              they do not make sense without it.
 * AGGREGATION: the Storage object is handed in from outside and could be
 *              replaced by another Storage without changing this class.
 */
public class ExpenseManager {

    private final ArrayList<Expense> expenses;
    private final Budget budget;
    private final Storage storage;

    public ExpenseManager(Storage storage) {
        this.expenses = new ArrayList<>();
        this.budget = new Budget();
        this.storage = storage;
    }

    // ---------- File handling ----------

    public void loadData() throws IOException {
        storage.load(expenses, budget);
    }

    public void saveData() throws IOException {
        storage.save(expenses, budget);
    }

    // ---------- Add (METHOD OVERLOADING) ----------

    /** Version 1: adds an Expense object that was already created (e.g. a RecurringExpense). */
    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    /** Version 2: builds a normal Expense from raw values, adds it and returns it. */
    public Expense addExpense(String description, double amount, Category category, LocalDate date) {
        Expense expense = new Expense(description, amount, category, date);
        expenses.add(expense);
        return expense;
    }

    // ---------- Search (METHOD OVERLOADING) ----------

    /** Search by ID: returns the expense, or null if there is none. */
    public Expense search(int id) {
        for (Expense e : expenses) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    /** Search by category: returns every expense in that category. */
    public ArrayList<Expense> search(Category category) {
        ArrayList<Expense> result = new ArrayList<>();
        for (Expense e : expenses) {
            if (e.getCategory() == category) {
                result.add(e);
            }
        }
        return result;
    }

    // ---------- Delete ----------

    /** Deletes the expense with this ID, or throws ExpenseNotFoundException. */
    public void deleteExpense(int id) throws ExpenseNotFoundException {
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getId() == id) {
                expenses.remove(i);
                return;
            }
        }
        throw new ExpenseNotFoundException(id);
    }

    // ---------- Totals (METHOD OVERLOADING: three versions of getTotal) ----------

    /** Total of all expenses. */
    public double getTotal() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        return total;
    }

    /** Total of one category. */
    public double getTotal(Category category) {
        double total = 0;
        for (Expense e : expenses) {
            if (e.getCategory() == category) {
                total += e.getAmount();
            }
        }
        return total;
    }

    /** Total of one month. */
    public double getTotal(int month, int year) {
        double total = 0;
        for (Expense e : expenses) {
            if (e.belongsToMonth(month, year)) {
                total += e.getAmount();
            }
        }
        return total;
    }

    /** Total spent in the current calendar month (used for the budget). */
    public double getCurrentMonthTotal() {
        LocalDate today = LocalDate.now();
        return getTotal(today.getMonthValue(), today.getYear());
    }

    /** The single largest expense, or null if the list is empty. */
    public Expense getHighestExpense() {
        Expense highest = null;
        for (Expense e : expenses) {
            if (highest == null || e.getAmount() > highest.getAmount()) {
                highest = e;
            }
        }
        return highest;
    }

    // ---------- Budget ----------

    public void setMonthlyBudget(double amount) {
        budget.setLimit(amount);
    }

    public Budget getBudget() {
        return budget;
    }

    // ---------- Information ----------

    /** Returns a COPY of the list so outside code cannot change the original (encapsulation). */
    public ArrayList<Expense> getAllExpenses() {
        return new ArrayList<>(expenses);
    }

    public int getCount() {
        return expenses.size();
    }

    public boolean isEmpty() {
        return expenses.isEmpty();
    }
}
