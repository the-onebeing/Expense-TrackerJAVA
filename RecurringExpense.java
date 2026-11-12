import java.time.LocalDate;

/**
 * RecurringExpense.java
 * An expense that repeats (rent, subscriptions, etc.). It extends Expense,
 * so it gets everything Expense has and adds a "frequency".
 *
 * Inheritance chain:  Transaction  ->  Expense  ->  RecurringExpense
 *
 * The same method call (getType, toFileString, toString) behaves differently
 * for a RecurringExpense than for a plain Expense: runtime polymorphism.
 */
public class RecurringExpense extends Expense {

    private String frequency;   // Weekly, Monthly or Yearly

    /** Used when loading from file (ID known). */
    public RecurringExpense(int id, String description, double amount,
                            Category category, LocalDate date, String frequency) {
        super(id, description, amount, category, date);
        setFrequency(frequency);
    }

    /** Used when the user adds a new recurring expense (ID auto-generated). */
    public RecurringExpense(String description, double amount,
                            Category category, LocalDate date, String frequency) {
        super(description, amount, category, date);
        setFrequency(frequency);
    }

    public String getFrequency() {
        return frequency;
    }

    public final void setFrequency(String frequency) {
        if (frequency == null) {
            throw new IllegalArgumentException("Frequency cannot be empty.");
        }
        String f = frequency.trim().toLowerCase();
        if (f.equals("weekly")) {
            this.frequency = "Weekly";
        } else if (f.equals("monthly")) {
            this.frequency = "Monthly";
        } else if (f.equals("yearly")) {
            this.frequency = "Yearly";
        } else {
            throw new IllegalArgumentException("Frequency must be Weekly, Monthly or Yearly.");
        }
    }

    // OVERRIDING: replaces the version inherited from Expense.
    @Override
    public String getType() {
        return "Recurring-" + frequency;
    }

    // OVERRIDING: a recurring expense is saved with an "R" tag and one extra field.
    @Override
    public String toFileString() {
        return "R|" + getId() + "|" + getDescription() + "|" + getAmount() + "|"
                + getCategory().name() + "|" + getDate() + "|" + frequency;
    }
    // toString() is NOT overridden here: it is inherited from Expense and
    // automatically shows the new type text because it calls getType().
}
