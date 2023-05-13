package ntnu.idatt2106.backend.exceptions;

/**
 * Custom exception class that represents a scenario when no groceries are found.
 */
public class NoGroceriesFound extends Exception {
    public NoGroceriesFound(String message) {
        super(message);
    }
}
