package ntnu.idatt2106.backend.controller;

import ntnu.idatt2106.backend.exceptions.CategoryNotFound;
import ntnu.idatt2106.backend.exceptions.NoGroceriesFound;
import ntnu.idatt2106.backend.exceptions.RefrigeratorNotFoundException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.ShoppingList;
import ntnu.idatt2106.backend.model.SubCategory;
import ntnu.idatt2106.backend.model.Unit;
import ntnu.idatt2106.backend.model.category.Category;
import ntnu.idatt2106.backend.model.dto.shoppingListElement.ShoppingListElementDTO;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingList;
import ntnu.idatt2106.backend.service.ShoppingListService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ShoppingListControllerTest {
    @Mock
    private ShoppingListService shoppingListService;
    @InjectMocks
    private ShoppingListController shoppingListController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void is_it_possible_to_create_a_shopping_list() throws RefrigeratorNotFoundException {
        when(shoppingListService.createShoppingList(anyLong())).thenReturn(1L);

        ResponseEntity<Long> response = shoppingListController.createShoppingList(1L);

        verify(shoppingListService).createShoppingList(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody());
    }

    @Test
    void throws_RefrigeratorNotFoundException_if_createShoppingList_is_called_with_an_invalid_refrigeratorId() throws RefrigeratorNotFoundException {
        when(shoppingListService.createShoppingList(anyLong())).thenThrow(new RefrigeratorNotFoundException("No refrigerator found with given refrigerator id"));

        assertThrows(RefrigeratorNotFoundException.class, () -> {
            shoppingListController.createShoppingList(1L);
        });

        verify(shoppingListService).createShoppingList(1L);
    }


    @Test
    void is_it_possible_to_get_groceries_from_shopping_list() throws NoGroceriesFound {
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
        Unit unit = Unit.builder()
                .id(1L)
                .name("Milk")
                .weight(1000)
                .build();
        GroceryShoppingList groceryShoppingList = GroceryShoppingList.builder()
                .grocery(grocery)
                .shoppingList(shoppingList)
                .quantity(1)
                .isRequest(false)
                .unit(unit)
                .build();

        List<ShoppingListElementDTO> expectedGroceries = new ArrayList<>();
        expectedGroceries.add(new ShoppingListElementDTO(groceryShoppingList));
        when(shoppingListService.getGroceries(anyLong(), Mockito.anyBoolean())).thenReturn(expectedGroceries);

        ResponseEntity<List<ShoppingListElementDTO>> response = shoppingListController.getGroceriesFromShoppingList(1L);

        verify(shoppingListService).getGroceries(1, false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedGroceries, response.getBody());
    }

    @Test
    void is_no_groceries_found_thrown_when_there_is_no_groceries_in_given_shopping_list() throws NoGroceriesFound {
        when(shoppingListService.getGroceries(anyLong(), Mockito.anyBoolean())).thenThrow(new NoGroceriesFound("No groceries is found"));

        assertThrows(NoGroceriesFound.class, () -> {
            shoppingListController.getGroceriesFromShoppingList(1L);
        });

        verify(shoppingListService).getGroceries(1L, false);
    }

    @Test
    void is_it_possible_to_get_categories_from_shopping_list() throws CategoryNotFound {
       Category category = new Category(1L, "Vegetables");

        List<Category> expectedCategories = new ArrayList<>();
        expectedCategories.add(category);
        when(shoppingListService.getCategories(anyLong())).thenReturn(expectedCategories);

        ResponseEntity<List<Category>> response = shoppingListController.getCategoriesFromShoppingList(1L);

        verify(shoppingListService).getCategories(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCategories, response.getBody());
    }

    @Test
    void is_category_not_found_thrown_when_there_is_no_groceries_in_given_shopping_list() throws NoGroceriesFound, CategoryNotFound {
        when(shoppingListService.getCategories(anyLong())).thenThrow(new CategoryNotFound("No groceries found and there is thereby no categories"));

        assertThrows(CategoryNotFound.class, () -> {
            shoppingListController.getCategoriesFromShoppingList(1L);
        });

        verify(shoppingListService).getCategories(1L);
    }
}
