package ntnu.idatt2106.backend.exceptions;
/**
 * Custom exception class that represents a scenario when a requested element is not found.
 */
public class NoSuchElementException extends Exception {
    public NoSuchElementException(String message) {
        super(message);
    }
}
