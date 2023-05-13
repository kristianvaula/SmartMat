package ntnu.idatt2106.backend.model;

import ntnu.idatt2106.backend.model.category.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubCategoryTest {
    @Test
    public void testEquals() {
        SubCategory cart1 = new SubCategory();
        cart1.setId(1L);

        SubCategory cart2 = new SubCategory();
        cart2.setId(1L);

        SubCategory cart3 = new SubCategory();
        cart3.setId(2L);

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        SubCategory cart1 = new SubCategory();
        cart1.setId(1L);

        SubCategory cart2 = new SubCategory();
        cart2.setId(1L);

        SubCategory cart3 = new SubCategory();
        cart3.setId(2L);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        long id = 1L;
        String name = "name";
        Category category = new Category();
        int expDays = 1;
        SubCategory subCategory = new SubCategory();
        subCategory.setId(id);
        subCategory.setCategory(category);
        subCategory.setName(name);
        subCategory.setCategoryExpiryDays(expDays);

        // Assert
        assertEquals(subCategory.getId(), id);
        assertEquals(subCategory.getCategory(), category);
        assertEquals(subCategory.getCategoryExpiryDays(), expDays);
        assertEquals(subCategory.getName(), name);
    }

    @Test
    public void testToString() {
        // Arrange
        long id = 1L;
        String name = "name";
        Category category = new Category();
        int expDays = 1;
        SubCategory subCategory = new SubCategory();
        subCategory.setId(id);
        subCategory.setCategory(category);
        subCategory.setName(name);
        subCategory.setCategoryExpiryDays(expDays);

        // Act
        String result = subCategory.toString();

        // Assert
        assertTrue(result.contains("id=" + id));
        assertTrue(result.contains("name=" + name));
        assertTrue(result.contains("category=" + category.toString()));
        assertTrue(result.contains("categoryExpiryDays=" + expDays));
    }

    @Test
    public void testSubCategoryBuilder() {
        long id = 1L;
        String name = "Test Sub Category";
        Category category = Category.builder().id(2L).name("Test Category").build();
        int categoryExpiryDays = 5;

        SubCategory subCategory = SubCategory.builder()
                .id(id)
                .name(name)
                .category(category)
                .categoryExpiryDays(categoryExpiryDays)
                .build();

        assertNotNull(subCategory);
        assertEquals(id, subCategory.getId());
        assertEquals(name, subCategory.getName());
        assertEquals(category, subCategory.getCategory());
        assertEquals(categoryExpiryDays, subCategory.getCategoryExpiryDays());

        String expectedToString = "SubCategory(id=" + id +
                ", name=" + name +
                ", category=" + category +
                ", categoryExpiryDays=" + categoryExpiryDays + ")";
        assertEquals(expectedToString, subCategory.toString());

        SubCategory updatedSubCategory = SubCategory.builder()
                .id(id)
                .name("Updated Sub Category")
                .category(category)
                .categoryExpiryDays(10)
                .build();

        assertEquals("Updated Sub Category", updatedSubCategory.getName());
        assertEquals(10, updatedSubCategory.getCategoryExpiryDays());
    }
}
