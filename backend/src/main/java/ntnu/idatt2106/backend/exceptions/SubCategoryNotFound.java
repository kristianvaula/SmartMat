package ntnu.idatt2106.backend.exceptions;

/**
 * Custom exception class that represents a scenario when a subcategory is not found.
 */
public class SubCategoryNotFound extends Exception {
    public SubCategoryNotFound(String message) {
        super(message);
    }
}