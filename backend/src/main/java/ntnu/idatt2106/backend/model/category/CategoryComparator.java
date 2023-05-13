package ntnu.idatt2106.backend.model.category;

import java.util.Comparator;

/**
 * Comparator class for categories name
 * Compares the categories based on their name, and can be used to sort categories on category name
 */
public class CategoryComparator implements Comparator<Category> {
    @Override
    public int compare(Category c1, Category c2) {
        return c1.getName().compareTo(c2.getName());
    }
}
