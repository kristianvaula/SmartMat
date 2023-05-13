package ntnu.idatt2106.backend.exceptions;
/**
 * Custom exception class that represents a scenario when a user is not found in the system.
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}