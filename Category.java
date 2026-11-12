/**
 * Category.java
 * An enum is a special class that holds a fixed set of constants.
 * Here it lists the six allowed expense categories, so nobody can
 * accidentally type "Fod" or "food " - only these six values exist.
 */
public enum Category {

    FOOD("Food"),
    TRAVEL("Travel"),
    SHOPPING("Shopping"),
    EDUCATION("Education"),
    ENTERTAINMENT("Entertainment"),
    OTHERS("Others");

    // Encapsulation: the label is private and read through a getter.
    private final String label;

    // Enum constructor (always private). Runs once for each constant above.
    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /** Converts a menu number (1-6) into a Category. */
    public static Category fromNumber(int number) {
        Category[] all = values();
        if (number < 1 || number > all.length) {
            throw new IllegalArgumentException("Category number must be between 1 and " + all.length);
        }
        return all[number - 1];
    }

    /** Prints the numbered category menu (1. Food, 2. Travel, ...). */
    public static void printMenu() {
        Category[] all = values();
        for (int i = 0; i < all.length; i++) {
            System.out.println("  " + (i + 1) + ". " + all[i].label);
        }
    }

    // Overrides Object.toString() so printing a Category shows "Food", not "FOOD".
    @Override
    public String toString() {
        return label;
    }
}
