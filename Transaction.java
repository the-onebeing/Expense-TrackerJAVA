import java.time.LocalDate;

/**
 * Transaction.java
 * ABSTRACT parent class holding what every money record has in common:
 * id, description, amount and date. You cannot write "new Transaction(...)"
 * because it is abstract - only child classes such as Expense can be created.
 *
 * Demonstrates: abstraction, encapsulation (private fields + getters/setters
 * with validation), constructors, interface implementation.
 */
public abstract class Transaction implements Recordable {

    // Encapsulation: fields are private, so other classes cannot change them directly.
    private int id;
    private String description;
    private double amount;
    private LocalDate date;

    // protected: only this class and its children can call this constructor.
    protected Transaction(int id, String description, double amount, LocalDate date) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number.");
        }
        this.id = id;
        setDescription(description);   // setters validate the values
        setAmount(amount);
        setDate(date);
    }

    // ---------- Getters ----------
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    // ---------- Setters with validation ----------
    // 'final' means a child class cannot override them (they are called from the constructor).
    public final void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
        // '|' is our file separator, so replace it to keep the save file readable.
        this.description = description.trim().replace('|', '/');
    }

    public final void setAmount(double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("Amount must be a positive number.");
        }
        this.amount = amount;
    }

    public final void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be empty.");
        }
        this.date = date;
    }

    /** True if this transaction happened in the given month and year. */
    public boolean belongsToMonth(int month, int year) {
        return date.getMonthValue() == month && date.getYear() == year;
    }

    /** Abstract method: no body here. Every child class MUST provide its own version. */
    public abstract String getType();
}
