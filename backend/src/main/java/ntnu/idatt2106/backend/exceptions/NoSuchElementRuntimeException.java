package ntnu.idatt2106.backend.exceptions;

/**
 * Custom runtime exception class that represents a scenario when a requested element is not found.
 */
public class NoSuchElementRuntimeException extends RuntimeException {
    public NoSuchElementRuntimeException(String message) {
        super(message);
    }
}