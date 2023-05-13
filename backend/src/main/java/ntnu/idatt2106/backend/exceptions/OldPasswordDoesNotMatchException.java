package ntnu.idatt2106.backend.exceptions;
/**
 * Custom exception class that represents a scenario when the old password does not match the one stored in the system.
 */
public class OldPasswordDoesNotMatchException extends Exception {
    public OldPasswordDoesNotMatchException(String message) {
        super(message);
    }
}