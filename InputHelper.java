import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * InputHelper.java
 * A utility class: every method is STATIC, so you call them directly as
 * InputHelper.readInt(...) without creating an object.
 * Each method keeps asking until the user types valid input, so the rest
 * of the program never has to worry about bad input.
 */
public class InputHelper {

    // One shared Scanner for the whole program (static variable).
    private static final Scanner scanner = new Scanner(System.in);

    // Private constructor: nobody should create an InputHelper object.
    private InputHelper() {
    }

    /** Reads a whole number between min and max (inclusive). */
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    if (max == Integer.MAX_VALUE) {
                        System.out.println("  Please enter a positive whole number.");
                    } else {
                        System.out.println("  Please enter a number between " + min + " and " + max + ".");
                    }
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a whole number.");
            }
        }
    }

    /** Reads a positive amount of money. Commas such as 1,500 are allowed. */
    public static double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim().replace(",", "");
            try {
                double value = Double.parseDouble(line);
                if (Double.isNaN(value) || Double.isInfinite(value) || value <= 0) {
                    System.out.println("  Amount must be greater than 0.");
                } else if (value > 1_000_000_000) {
                    System.out.println("  Amount is too large.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a number like 250 or 99.50.");
            }
        }
    }

    /** Reads a non-empty line of text. */
    public static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println("  This field cannot be empty.");
            } else {
                return line;
            }
        }
    }

    /** Reads a date as yyyy-MM-dd. Pressing Enter gives today's date. Future dates are rejected. */
    public static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                return LocalDate.now();
            }
            try {
                LocalDate date = LocalDate.parse(line);
                if (date.isAfter(LocalDate.now())) {
                    System.out.println("  Date cannot be in the future.");
                } else {
                    return date;
                }
            } catch (DateTimeParseException e) {
                System.out.println("  Invalid date. Use the format yyyy-MM-dd (example: 2026-11-05).");
            }
        }
    }

    /** Shows the category menu and returns the chosen Category. */
    public static Category readCategory() {
        System.out.println("Choose a category:");
        Category.printMenu();
        int choice = readInt("Category number: ", 1, Category.values().length);
        return Category.fromNumber(choice);
    }

    /** Reads y or n. */
    public static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim().toLowerCase();
            if (line.equals("y") || line.equals("yes")) {
                return true;
            }
            if (line.equals("n") || line.equals("no")) {
                return false;
            }
            System.out.println("  Please type y or n.");
        }
    }

    /** Asks how often a recurring expense repeats. */
    public static String readFrequency() {
        System.out.println("How often does it repeat?");
        System.out.println("  1. Weekly");
        System.out.println("  2. Monthly");
        System.out.println("  3. Yearly");
        int choice = readInt("Choice: ", 1, 3);
        if (choice == 1) {
            return "Weekly";
        } else if (choice == 2) {
            return "Monthly";
        }
        return "Yearly";
    }
}
