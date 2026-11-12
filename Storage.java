import java.io.IOException;
import java.util.List;

/**
 * Storage.java
 * Interface describing WHAT a storage system must do (save and load)
 * without saying HOW. FileStorage saves to a text file; later someone
 * could write DatabaseStorage without changing ExpenseManager at all.
 */
public interface Storage {

    /** Saves all expenses and the monthly budget. */
    void save(List<Expense> expenses, Budget budget) throws IOException;

    /** Loads saved data INTO the list and budget object that are passed in. */
    void load(List<Expense> expenses, Budget budget) throws IOException;
}
