package ntnu.idatt2106.backend.model.grocery;

import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.SubCategory;
import ntnu.idatt2106.backend.model.Unit;
import ntnu.idatt2106.backend.model.category.Category;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RefrigeratorGroceryTest {
    @Test
    void is_it_possible_to_create_a_refrigeratorGrocery_with_the_constructor() {
        Refrigerator refrigerator = Refrigerator.builder()
                .id(1L)
                .name("test")
                .address("ntnu")
                .build();

        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        Grocery grocery = Grocery.builder()
                .id(1L)
                .name("Milk")
                .description("Description")
                .groceryExpiryDays(1)
                .subCategory(subCategory)
                .build();
        Unit unit = Unit.builder()
                .id(1L)
                .name("l")
                .weight(1000)
                .build();
        RefrigeratorGrocery refrigeratorGrocery = new RefrigeratorGrocery(1L, LocalDate.now(), refrigerator, grocery, unit, 1, new HashSet<>());

        assertEquals(1L, refrigeratorGrocery.getId());
        assertEquals(LocalDate.now(), refrigeratorGrocery.getPhysicalExpireDate());
        assertEquals(refrigerator, refrigeratorGrocery.getRefrigerator());
        assertEquals(grocery, refrigeratorGrocery.getGrocery());
        assertEquals(unit, refrigeratorGrocery.getUnit());
        assertEquals(1, refrigeratorGrocery.getQuantity());
    }

    @Test
    void does_equal_method_return_true_for_two_similar_refrigeratorGroceries_objects() {
        Refrigerator refrigerator = Refrigerator.builder()
                .id(1L)
                .name("test")
                .address("ntnu")
                .build();

        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        Grocery grocery = Grocery.builder()
                .id(1L)
                .name("Milk")
                .description("Description")
                .groceryExpiryDays(1)
                .subCategory(subCategory)
                .build();
        Unit unit = Unit.builder()
                .id(1L)
                .name("l")
                .weight(1000)
                .build();
        RefrigeratorGrocery refrigeratorGrocery1 = new RefrigeratorGrocery(1L, LocalDate.now(), refrigerator, grocery, unit, 1, new HashSet<>());
        RefrigeratorGrocery refrigeratorGrocery2 = new RefrigeratorGrocery(1L, LocalDate.now(), refrigerator, grocery, unit, 1, new HashSet<>());

        assertEquals(refrigeratorGrocery1, refrigeratorGrocery2);
    }

    @Test
    void does_equal_method_return_false_for_two_different_refrigeratorGrocery_objects() {
        Refrigerator refrigerator = Refrigerator.builder()
                .id(1L)
                .name("test")
                .address("ntnu")
                .build();
        Refrigerator refrigerator2 = new Refrigerator();

        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        Grocery grocery = Grocery.builder()
                .id(1L)
                .name("Milk")
                .description("Description")
                .groceryExpiryDays(1)
                .subCategory(subCategory)
                .build();
        Grocery grocery2 = new Grocery();
        Unit unit = Unit.builder()
                .id(1L)
                .name("l")
                .weight(1000)
                .build();
        Unit unit2 = new Unit();
        RefrigeratorGrocery refrigeratorGrocery1 = new RefrigeratorGrocery(1L, LocalDate.now(), refrigerator, grocery, unit, 1, new HashSet<>());
        RefrigeratorGrocery refrigeratorGrocery2 = new RefrigeratorGrocery(1L, LocalDate.now(), refrigerator2, grocery2, unit2, 2, new HashSet<>());

        assertNotEquals(refrigeratorGrocery1, refrigeratorGrocery2);
    }

    @Test
    void does_hash_method_return_true_for_two_similar_refrigeratorGroceries_objects() {
        Refrigerator refrigerator = Refrigerator.builder()
                .id(1L)
                .name("test")
                .address("ntnu")
                .build();

        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        Grocery grocery = Grocery.builder()
                .id(1L)
                .name("Milk")
                .description("Description")
                .groceryExpiryDays(1)
                .subCategory(subCategory)
                .build();
        Unit unit = Unit.builder()
                .id(1L)
                .name("l")
                .weight(1000)
                .build();
        RefrigeratorGrocery refrigeratorGrocery1 = new RefrigeratorGrocery(1L, LocalDate.now(), refrigerator, grocery, unit, 1, new HashSet<>());
        RefrigeratorGrocery refrigeratorGrocery2 = new RefrigeratorGrocery(1L, LocalDate.now(), refrigerator, grocery, unit, 1, new HashSet<>());

        assertEquals(refrigeratorGrocery1.hashCode(), refrigeratorGrocery2.hashCode());
    }

    @Test
    void does_hash_method_return_false_for_two_different_refrigeratorGrocery_objects() {
        Refrigerator refrigerator = Refrigerator.builder()
                .id(1L)
                .name("test")
                .address("ntnu")
                .build();
        Refrigerator refrigerator2 = new Refrigerator();

        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        Grocery grocery = Grocery.builder()
                .id(1L)
                .name("Milk")
                .description("Description")
                .groceryExpiryDays(1)
                .subCategory(subCategory)
                .build();
        Grocery grocery2 = new Grocery();
        Unit unit = Unit.builder()
                .id(1L)
                .name("l")
                .weight(1000)
                .build();
        Unit unit2 = new Unit();
        RefrigeratorGrocery refrigeratorGrocery1 = new RefrigeratorGrocery(1L, LocalDate.now(), refrigerator, grocery, unit, 1, new HashSet<>());
        RefrigeratorGrocery refrigeratorGrocery2 = new RefrigeratorGrocery(1L, LocalDate.now(), refrigerator2, grocery2, unit2, 2, new HashSet<>());

        assertNotEquals(refrigeratorGrocery1.hashCode(), refrigeratorGrocery2.hashCode());
    }

    @Test
    void does_getter_for_physicalExpireDate_return_the_correct_date() {
        Refrigerator refrigerator = Refrigerator.builder()
                .id(1L)
                .name("test")
                .address("ntnu")
                .build();
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        Grocery grocery = Grocery.builder()
                .id(1L)
                .name("Milk")
                .description("Description")
                .groceryExpiryDays(1)
                .subCategory(subCategory)
                .build();
        Unit unit = Unit.builder()
                .id(1L)
                .name("l")
                .weight(1000)
                .build();
        RefrigeratorGrocery refrigeratorGrocery = new RefrigeratorGrocery(1L, LocalDate.now(), refrigerator, grocery, unit, 1, new HashSet<>());
        assertEquals(LocalDate.now(), refrigeratorGrocery.getPhysicalExpireDate());
    }

    @Test
    void does_setter_for_physicalExpireDate_set_the_correct_date() {
        RefrigeratorGrocery refrigeratorGrocery = new RefrigeratorGrocery();
        refrigeratorGrocery.setPhysicalExpireDate(LocalDate.now());
        assertEquals(LocalDate.now(), refrigeratorGrocery.getPhysicalExpireDate());
    }

    @Test
    void does_getter_refrigerator_return_the_correct_refrigerator() {
        Refrigerator refrigerator = Refrigerator.builder()
                .id(1L)
                .name("test")
                .address("ntnu")
                .build();
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        Grocery grocery = Grocery.builder()
                .id(1L)
                .name("Milk")
                .description("Description")
                .groceryExpiryDays(1)
                .subCategory(subCategory)
                .build();
        Unit unit = Unit.builder()
                .id(1L)
                .name("l")
                .weight(1000)
                .build();
        RefrigeratorGrocery refrigeratorGrocery = new RefrigeratorGrocery(1L, LocalDate.now(), refrigerator, grocery, unit, 1, new HashSet<>());
        assertEquals(refrigerator, refrigeratorGrocery.getRefrigerator());
    }

    @Test
    void does_setter_for_refrigerator_set_the_correct_value() {
        Refrigerator refrigerator = Refrigerator.builder()
                .id(1L)
                .name("test")
                .address("ntnu")
                .build();
        RefrigeratorGrocery refrigeratorGrocery = RefrigeratorGrocery.builder()
                        .refrigerator(refrigerator)
                        .build();
        assertEquals(refrigerator, refrigeratorGrocery.getRefrigerator());
    }

    @Test
    void does_getter_grocery_return_the_correct_grocery() {
        Refrigerator refrigerator = Refrigerator.builder()
                .id(1L)
                .name("test")
                .address("ntnu")
                .build();
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        Grocery grocery = Grocery.builder()
                .id(1L)
                .name("Milk")
                .description("Description")
                .groceryExpiryDays(1)
                .subCategory(subCategory)
                .build();
        Unit unit = Unit.builder()
                .id(1L)
                .name("l")
                .weight(1000)
                .build();
        RefrigeratorGrocery refrigeratorGrocery = new RefrigeratorGrocery(1L, LocalDate.now(), refrigerator, grocery, unit, 1, new HashSet<>());
        assertEquals(grocery, refrigeratorGrocery.getGrocery());
    }

    @Test
    void does_setter_for_grocery_set_the_correct_value() {
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        Grocery grocery = Grocery.builder()
                .id(1L)
                .name("Milk")
                .description("Description")
                .groceryExpiryDays(1)
                .subCategory(subCategory)
                .build();
        RefrigeratorGrocery refrigeratorGrocery = RefrigeratorGrocery.builder()
                .grocery(grocery)
                .build();
        assertEquals(grocery, refrigeratorGrocery.getGrocery());
    }

    @Test
    void does_getter_unit_return_the_correct_unit() {
        Refrigerator refrigerator = Refrigerator.builder()
                .id(1L)
                .name("test")
                .address("ntnu")
                .build();
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        Grocery grocery = Grocery.builder()
                .id(1L)
                .name("Milk")
                .description("Description")
                .groceryExpiryDays(1)
                .subCategory(subCategory)
                .build();
        Unit unit = Unit.builder()
                .id(1L)
                .name("l")
                .weight(1000)
                .build();
        RefrigeratorGrocery refrigeratorGrocery = new RefrigeratorGrocery(1L, LocalDate.now(), refrigerator, grocery, unit, 1, new HashSet<>());
        assertEquals(unit, refrigeratorGrocery.getUnit());
    }

    @Test
    void does_setter_for_unit_set_the_correct_value() {
        Unit unit = Unit.builder()
                .id(1L)
                .name("l")
                .weight(1000)
                .build();
        RefrigeratorGrocery refrigeratorGrocery = RefrigeratorGrocery.builder()
                .unit(unit)
                .build();
        assertEquals(unit, refrigeratorGrocery.getUnit());
    }

    @Test
    void does_getter_quantity_return_the_correct_quantity() {
        Refrigerator refrigerator = Refrigerator.builder()
                .id(1L)
                .name("test")
                .address("ntnu")
                .build();
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        Grocery grocery = Grocery.builder()
                .id(1L)
                .name("Milk")
                .description("Description")
                .groceryExpiryDays(1)
                .subCategory(subCategory)
                .build();
        Unit unit = Unit.builder()
                .id(1L)
                .name("l")
                .weight(1000)
                .build();
        RefrigeratorGrocery refrigeratorGrocery = new RefrigeratorGrocery(1L, LocalDate.now(), refrigerator, grocery, unit, 1, new HashSet<>());
        assertEquals(1, refrigeratorGrocery.getQuantity());
    }

    @Test
    void does_setter_for_quantity_set_the_correct_value() {
        RefrigeratorGrocery refrigeratorGrocery = RefrigeratorGrocery.builder()
                .quantity(1)
                .build();
        assertEquals(1, refrigeratorGrocery.getQuantity());
    }
}
