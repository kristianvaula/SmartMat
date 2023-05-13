package ntnu.idatt2106.backend.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ntnu.idatt2106.backend.model.grocery.GroceryNotification;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ntnu.idatt2106.backend.model.User;

public class GroceryNotificationTest {

    private GroceryNotification groceryNotification;
    private User user;
    private RefrigeratorGrocery grocery;

    @BeforeEach
    void setUp() {
        user = new User();
        grocery = new RefrigeratorGrocery();
        groceryNotification = GroceryNotification.builder()
                .user(user)
                .groceryEntity(grocery)
                .daysLeft(5L)
                .deleted(false)
                .build();
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(user, groceryNotification.getUser());
        assertEquals(grocery, groceryNotification.getGroceryEntity());
        assertEquals(5L, groceryNotification.getDaysLeft());
        assertFalse(groceryNotification.isDeleted());

        User newUser = new User();
        groceryNotification.setUser(newUser);
        assertEquals(newUser, groceryNotification.getUser());

        RefrigeratorGrocery newGrocery = new RefrigeratorGrocery();
        groceryNotification.setGroceryEntity(newGrocery);
        assertEquals(newGrocery, groceryNotification.getGroceryEntity());

        groceryNotification.setDaysLeft(7L);
        assertEquals(7L, groceryNotification.getDaysLeft());

        groceryNotification.setDeleted(true);
        assertTrue(groceryNotification.isDeleted());
    }

    @Test
    void testIdGeneration() {
        assertEquals(0L, groceryNotification.getId());

        GroceryNotification notificationWithId = GroceryNotification.builder()
                .id(123L)
                .user(user)
                .groceryEntity(grocery)
                .daysLeft(5L)
                .deleted(false)
                .build();

        assertEquals(123L, notificationWithId.getId());
    }

    @Test
    void testEqualsMethod() {
        // Create two identical GroceryNotifications
        GroceryNotification notification1 = GroceryNotification.builder()
                .user(user)
                .groceryEntity(grocery)
                .daysLeft(5L)
                .deleted(false)
                .build();
        GroceryNotification notification2 = GroceryNotification.builder()
                .user(user)
                .groceryEntity(grocery)
                .daysLeft(5L)
                .deleted(false)
                .build();

        // Test that they are equal
        assertEquals(notification1, notification2);
    }

    @Test
    void testHashCodeMethod() {
        // Create two identical GroceryNotifications
        GroceryNotification notification1 = GroceryNotification.builder()
                .user(user)
                .groceryEntity(grocery)
                .daysLeft(5L)
                .deleted(false)
                .build();
        GroceryNotification notification2 = GroceryNotification.builder()
                .user(user)
                .groceryEntity(grocery)
                .daysLeft(5L)
                .deleted(false)
                .build();

        // Test that their hash codes are equal
        assertEquals(notification1.hashCode(), notification2.hashCode());
    }


}
