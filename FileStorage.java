import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * FileStorage.java
 * Saves and loads data using a plain text file (default: expenses.txt).
 * Implements the Storage interface.
 *
 * File format (one record per line, fields separated by '|'):
 *   BUDGET|5000.0
 *   E|1|Lunch|120.0|FOOD|2026-11-05
 *   R|2|Netflix|499.0|ENTERTAINMENT|2026-11-01|Monthly
 */
public class FileStorage implements Storage {

    private final String fileName;

    /** Default constructor: uses the file name "expenses.txt". */
    public FileStorage() {
        this("expenses.txt");
    }

    /** Overloaded constructor: lets the caller choose the file name. */
    public FileStorage(String fileName) {
        this.fileName = fileName;
    }

    /** Writes the budget line and then one line per expense. */
    @Override
    public void save(List<Expense> expenses, Budget budget) throws IOException {
        // try-with-resources closes the file automatically, even if an error occurs.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("BUDGET|" + budget.getLimit());
            writer.newLine();
            for (Expense e : expenses) {
                writer.write(e.toFileString());   // polymorphism: Expense or RecurringExpense version
                writer.newLine();
            }
        }
    }

    /** Reads the file line by line. A missing file simply means "first run". */
    @Override
    public void load(List<Expense> expenses, Budget budget) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }

        int skipped = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    parseLine(line, expenses, budget);
                } catch (IllegalArgumentException | DateTimeParseException
                         | ArrayIndexOutOfBoundsException ex) {
                    skipped++;   // a damaged line should not stop the whole program
                }
            }
        }
        if (skipped > 0) {
            System.out.println("Warning: " + skipped + " damaged line(s) in "
                    + fileName + " were skipped.");
        }
    }

    // Turns one line of text back into an object. Throws an exception if the line is bad.
    private void parseLine(String line, List<Expense> expenses, Budget budget) {
        String[] p = line.split("\\|");
        String kind = p[0];

        if (kind.equals("BUDGET")) {
            budget.setLimit(Double.parseDouble(p[1]));
            return;
        }

        int id = Integer.parseInt(p[1]);
        String description = p[2];
        double amount = Double.parseDouble(p[3]);
        Category category = Category.valueOf(p[4]);
        LocalDate date = LocalDate.parse(p[5]);

        for (Expense existing : expenses) {
            if (existing.getId() == id) {
                throw new IllegalArgumentException("Duplicate ID " + id);
            }
        }

        if (kind.equals("E")) {
            expenses.add(new Expense(id, description, amount, category, date));
        } else if (kind.equals("R")) {
            expenses.add(new RecurringExpense(id, description, amount, category, date, p[6]));
        } else {
            throw new IllegalArgumentException("Unknown record type: " + kind);
        }
    }
}
