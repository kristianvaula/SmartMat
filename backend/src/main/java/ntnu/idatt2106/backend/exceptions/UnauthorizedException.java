package ntnu.idatt2106.backend.exceptions;
/**
 * Custom exception class that represents a scenario when an unauthorized action is attempted.
 */
public class UnauthorizedException extends Exception {
    public UnauthorizedException(String message) {
        super(message);
    }
}