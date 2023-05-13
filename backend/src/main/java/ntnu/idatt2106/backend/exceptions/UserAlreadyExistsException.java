package ntnu.idatt2106.backend.exceptions;
/**
 * Custom exception class that represents a scenario when a user already exists in the system.
 */
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}