package ntnu.idatt2106.backend.model;

import ntnu.idatt2106.backend.model.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {
    @Test
    public void testEquals() {
        User cart1 = new User();
        cart1.setId("1L");

        User cart2 = new User();
        cart2.setId("1L");

        User cart3 = new User();
        cart3.setId("2L");

        assertEquals(cart1, cart2); // Test if two shopping carts with the same ID are equal
        assertNotEquals(cart1, cart3); // Test if two shopping carts with different IDs are not equal
    }

    @Test
    public void testHashCode() {
        User cart1 = new User();
        cart1.setId("1L");

        User cart2 = new User();
        cart2.setId("1L");

        User cart3 = new User();
        cart3.setId("2L");

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Test if two shopping carts with the same ID have the same hash code
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Test if two shopping carts with different IDs have different hash codes
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        String id = "1L";
        String name = "name";
        String email = "address";
        String password = "password";
        UserRole userRole = UserRole.USER;
        long refrigeratorId = 1L;
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserRole(userRole);
        user.setFavoriteRefrigeratorId(refrigeratorId);

        // Assert
        assertEquals(user.getId(), id);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getName(), name);
        assertEquals(user.getUsername(), email);
        assertEquals(user.getFavoriteRefrigeratorId(), refrigeratorId);
        assertEquals(user.getUserRole(), userRole);
        assertEquals(user.getAuthorities(), List.of(new SimpleGrantedAuthority(userRole.name())));
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isEnabled());
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    public void testToString() {
        // Arrange
        String id = "1L";
        String name = "name";
        String email = "address";
        String password = "password";
        UserRole userRole = UserRole.USER;
        long refrigeratorId = 1L;
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserRole(userRole);
        user.setFavoriteRefrigeratorId(refrigeratorId);

        // Act
        String result = user.toString();

        // Assert
        assertTrue(result.contains("id=" + id));
        assertTrue(result.contains("name=" + name));
        assertTrue(result.contains("email=" + email));
        assertTrue(result.contains("password=" + password));
        assertTrue(result.contains("userRole=" + userRole));
        assertTrue(result.contains("favoriteRefrigeratorId=" + refrigeratorId));
    }

    @Test
    public void testUserBuilder() {
        String userId = "user1";
        String name = "John Doe";
        String email = "john.doe@example.com";
        String password = "password";
        UserRole role = UserRole.USER;
        Long favoriteRefrigeratorId = 1L;

        User user = User.builder()
                .id(userId)
                .name(name)
                .email(email)
                .password(password)
                .userRole(role)
                .favoriteRefrigeratorId(favoriteRefrigeratorId)
                .build();

        assertEquals(userId, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(role, user.getUserRole());
        assertEquals(favoriteRefrigeratorId, user.getFavoriteRefrigeratorId());
    }
}
