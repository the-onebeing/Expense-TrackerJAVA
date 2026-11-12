/**
 * Budget.java
 * Stores the monthly spending limit and does the budget calculations.
 * A limit of 0 means "no budget has been set yet".
 */
public class Budget {

    private double monthlyLimit;

    /** Default constructor: no budget set. */
    public Budget() {
        this(0);
    }

    /** Constructor with a starting limit. */
    public Budget(double monthlyLimit) {
        setLimit(monthlyLimit);
    }

    public double getLimit() {
        return monthlyLimit;
    }

    public void setLimit(double monthlyLimit) {
        if (Double.isNaN(monthlyLimit) || Double.isInfinite(monthlyLimit) || monthlyLimit < 0) {
            throw new IllegalArgumentException("Budget cannot be negative.");
        }
        this.monthlyLimit = monthlyLimit;
    }

    public boolean isSet() {
        return monthlyLimit > 0;
    }

    /** Remaining balance = budget - amount spent (negative means overspent). */
    public double getRemaining(double spent) {
        return monthlyLimit - spent;
    }

    public boolean isExceeded(double spent) {
        return isSet() && spent > monthlyLimit;
    }

    /** Percentage of the budget already used. */
    public double getUsedPercent(double spent) {
        if (!isSet()) {
            return 0;
        }
        return spent / monthlyLimit * 100;
    }
}
