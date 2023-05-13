package ntnu.idatt2106.backend.model.category;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void is_it_possible_to_create_a_category_with_the_constructor() {
        Category category = new Category(1, "Test Category");

        long categoryId = category.getId();
        String categoryName = category.getName();

        assertEquals(1, categoryId);
        assertEquals("Test Category", categoryName);
    }

    @Test
    void does_the_to_string_method_return_expected_result() {
        Category category = new Category(1, "Test Category");
        String expectedString = "Category(id=1, name=Test Category)";

        String result = category.toString();

        assertEquals(expectedString, result);
    }

    @Test
    void does_equal_method_return_true_for_two_similar_category_objects() {
        Category category1 = new Category(1, "Test Category");
        Category category2 = new Category(1, "Test Category");

        assertEquals(category1, category2);
    }

    @Test
    void does_equal_method_return_false_for_two_different_category_objects() {
        Category category1 = new Category(1, "Test Category");
        Category category2 = new Category(2, "Test Category");

        assertNotEquals(category1, category2);
    }

    @Test
    void does_hash_method_return_true_for_two_similar_category_objects() {
        Category category1 = new Category(1, "Test Category");
        Category category2 = new Category(1, "Test Category");

        assertEquals(category1.hashCode(), category2.hashCode());
    }


    @Test
    void does_hash_method_return_false_for_two_different_category_objects() {
        Category category1 = new Category(1, "Test Category");
        Category category2 = new Category(2, "Test Category");

        assertNotEquals(category1.hashCode(), category2.hashCode());
    }


    @Test
    void does_getter_for_categoryName_return_the_correct_name() {
        Category category = new Category(1, "Test Category");
        assertEquals("Test Category", category.getName());
    }

    @Test
    void does_setter_for_categoryName_set_the_name_correct() {
        Category category = new Category(1, "Test Category");
        category.setName("New Category");
        assertEquals("New Category", category.getName());
    }

    @Test
    void testCategoryBuilder() {
        Category category = Category.builder().id(1).name("Test Category 2").build();
        assertEquals(1, category.getId());
        assertEquals("Test Category 2", category.getName());
    }
}
