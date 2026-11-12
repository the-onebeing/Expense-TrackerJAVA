import java.time.LocalDate;

/**
 * Expense.java
 * A normal (one-time) expense. It INHERITS id, description, amount and date
 * from Transaction and ADDS a category.
 *
 * Demonstrates: inheritance, constructor overloading (compile-time
 * polymorphism), method overriding (runtime polymorphism), static variable
 * and static method, encapsulation.
 */
public class Expense extends Transaction {

    // STATIC variable: ONE copy shared by all Expense objects.
    // It remembers the next free ID so every new expense gets a unique ID.
    private static int nextId = 1;

    private Category category;

    // ---------- Constructors (OVERLOADING: same name, different parameters) ----------

    /** Full constructor. Used when loading from file (the ID is already known). */
    public Expense(int id, String description, double amount, Category category, LocalDate date) {
        super(id, description, amount, date);   // calls the Transaction constructor
        setCategory(category);
        if (id >= nextId) {                     // keep nextId ahead of every ID in use
            nextId = id + 1;
        }
    }

    /** Auto-generates the ID. Used when the user adds a new expense. */
    public Expense(String description, double amount, Category category, LocalDate date) {
        this(nextId, description, amount, category, date);   // calls the constructor above
    }

    /** Date not supplied, so today's date is used. */
    public Expense(String description, double amount, Category category) {
        this(description, amount, category, LocalDate.now());
    }

    // ---------- Category getter/setter ----------
    public Category getCategory() {
        return category;
    }

    public final void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }
        this.category = category;
    }

    // ---------- Static helpers ----------

    /** STATIC method: belongs to the class, so no object is needed to call it. */
    public static int getNextId() {
        return nextId;
    }

    /** The heading line of the expense table. */
    public static String tableHeader() {
        return String.format("%-5s %-24s %12s  %-15s %-12s %-17s",
                "ID", "Description", "Amount (Rs)", "Category", "Date", "Type");
    }

    // ---------- Overridden methods (RUNTIME polymorphism) ----------

    /** Overrides the abstract method declared in Transaction. */
    @Override
    public String getType() {
        return "One-time";
    }

    /** Implements the Recordable interface: E|id|description|amount|CATEGORY|date */
    @Override
    public String toFileString() {
        return "E|" + getId() + "|" + getDescription() + "|" + getAmount() + "|"
                + category.name() + "|" + getDate();
    }

    /** Overrides Object.toString(): one formatted table row. */
    @Override
    public String toString() {
        return String.format("%-5d %-24s %12.2f  %-15s %-12s %s",
                getId(), shorten(getDescription(), 24), getAmount(),
                category.getLabel(), getDate(), getType());
    }

    // Private helper: cuts long text so the table stays aligned.
    private String shorten(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
}
