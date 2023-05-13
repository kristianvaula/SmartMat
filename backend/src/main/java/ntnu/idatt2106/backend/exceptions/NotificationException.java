package ntnu.idatt2106.backend.exceptions;

/**
 * Custom exception class that represents a scenario when an error occurs related to notifications.
 */
public class NotificationException extends Exception {
    public NotificationException(String message) {
        super(message);
    }
}