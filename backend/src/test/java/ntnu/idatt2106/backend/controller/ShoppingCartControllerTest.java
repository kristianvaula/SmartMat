package ntnu.idatt2106.backend.controller;

import ntnu.idatt2106.backend.exceptions.NoGroceriesFound;
import ntnu.idatt2106.backend.exceptions.RefrigeratorNotFoundException;
import ntnu.idatt2106.backend.exceptions.ShoppingCartNotFound;
import ntnu.idatt2106.backend.exceptions.ShoppingListNotFound;
import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.model.category.Category;
import ntnu.idatt2106.backend.model.dto.shoppingCartElement.ShoppingCartElementDTO;
import ntnu.idatt2106.backend.model.dto.shoppingListElement.ShoppingListElementDTO;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingList;
import ntnu.idatt2106.backend.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ShoppingCartControllerTest {

    @Mock
    private ShoppingCartService shoppingCartService;
    @InjectMocks
    private ShoppingCartController shoppingCartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void is_it_possible_to_create_a_shopping_cart() throws ShoppingListNotFound {
        when(shoppingCartService.createShoppingCart(anyLong())).thenReturn(1L);

        ResponseEntity<Long> response = shoppingCartController.createShoppingCartId(1L);

        verify(shoppingCartService).createShoppingCart(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody());
    }

    @Test
    void throws_ShoppingListNotFound_if_createShoppingCart_is_called_with_an_invalid_shoppingListId() throws ShoppingListNotFound {
        when(shoppingCartService.createShoppingCart(anyLong())).thenThrow(new ShoppingListNotFound("No shopping list found with given shopping list id"));

        assertThrows(ShoppingListNotFound.class, () -> {
            shoppingCartController.createShoppingCartId(1L);
        });

        verify(shoppingCartService).createShoppingCart(1L);
    }

    @Test
    void is_it_possible_to_get_groceries_from_shopping_cart() throws NoGroceriesFound {
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        Grocery grocery = Grocery.builder()
                .name("Milk")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        Refrigerator refrigerator = Refrigerator.builder()
                .id(1L)
                .name("test")
                .address("ntnu")
                .build();
        ShoppingList shoppingList = ShoppingList.builder()
                .id(1L)
                .refrigerator(refrigerator)
                .build();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .id(1L)
                .shoppingList(shoppingList)
                .build();
        Unit unit = Unit.builder()
                .id(1L)
                .name("Milk")
                .weight(1000)
                .build();
        GroceryShoppingCart groceryShoppingCart = GroceryShoppingCart.builder()
                .grocery(grocery)
                .shoppingCart(shoppingCart)
                .quantity(1)
                .unit(unit)
                .build();

        List<ShoppingCartElementDTO> expectedGroceries = new ArrayList<>();
        expectedGroceries.add(new ShoppingCartElementDTO(groceryShoppingCart));
        when(shoppingCartService.getGroceries(anyLong())).thenReturn(expectedGroceries);

        ResponseEntity<List<ShoppingCartElementDTO>> response = shoppingCartController.getGroceriesFromShoppingCart(1L);

        verify(shoppingCartService).getGroceries(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedGroceries, response.getBody());
    }

    @Test
    void is_no_groceries_found_thrown_when_there_is_no_groceries_in_given_shopping_cart() throws NoGroceriesFound {
        when(shoppingCartService.getGroceries(anyLong())).thenThrow(new NoGroceriesFound("No groceries is found"));

        assertThrows(NoGroceriesFound.class, () -> {
            shoppingCartController.getGroceriesFromShoppingCart(1L);
        });

        verify(shoppingCartService).getGroceries(1L);
    }
}
