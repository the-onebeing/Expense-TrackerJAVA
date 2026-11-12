/**
 * Recordable.java
 * An INTERFACE is a contract: any class that implements it promises to
 * provide the methods listed here. Anything that can be saved to a file
 * as one line of text must be "Recordable".
 */
public interface Recordable {

    /** Returns the object as a single line of text suitable for saving in a file. */
    String toFileString();
}
