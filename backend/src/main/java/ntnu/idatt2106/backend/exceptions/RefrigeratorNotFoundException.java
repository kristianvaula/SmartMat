package ntnu.idatt2106.backend.exceptions;

/**
 * Custom exception class that represents a scenario when a refrigerator is not found.
 */
public class RefrigeratorNotFoundException extends Exception {
    public RefrigeratorNotFoundException(String message) {
        super(message);
    }
}
