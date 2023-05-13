package ntnu.idatt2106.backend.exceptions;

/**
 * Custom exception class that represents a scenario when a shopping list is not found.
 */
public class ShoppingListNotFound extends Exception {
    public ShoppingListNotFound(String message) {
        super(message);
    }
}
