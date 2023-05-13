package ntnu.idatt2106.backend.exceptions;

/**
 * Custom exception class that represents a scenario when an error occurs while saving data.
 */
public class SaveException extends Exception {
    public SaveException(String message) {
        super(message);
    }
}