/**
 * ExpenseNotFoundException.java
 * A CUSTOM (user-defined) checked exception. It is thrown when the user
 * tries to delete an expense ID that does not exist. Because it extends
 * Exception (a checked exception), the compiler forces callers to handle it.
 */
public class ExpenseNotFoundException extends Exception {

    public ExpenseNotFoundException(int id) {
        super("No expense found with ID " + id + ".");
    }
}
