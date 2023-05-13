package ntnu.idatt2106.backend.service;

import ntnu.idatt2106.backend.exceptions.*;
import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.model.category.Category;
import ntnu.idatt2106.backend.model.dto.CreateRefrigeratorGroceryDTO;
import ntnu.idatt2106.backend.model.dto.UnitDTO;
import ntnu.idatt2106.backend.model.dto.shoppingCartElement.ShoppingCartElementDTO;
import ntnu.idatt2106.backend.model.dto.shoppingListElement.ShoppingListElementDTO;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingList;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorShoppingList;
import ntnu.idatt2106.backend.model.requests.SaveGroceryListRequest;
import ntnu.idatt2106.backend.model.requests.SaveGroceryRequest;
import ntnu.idatt2106.backend.repository.GroceryShoppingCartRepository;
import ntnu.idatt2106.backend.repository.ShoppingCartRepository;
import ntnu.idatt2106.backend.repository.ShoppingListRepository;
import ntnu.idatt2106.backend.repository.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ShoppingCartServiceTest {

    @Mock
    private ShoppingListRepository shoppingListRepository;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private GroceryShoppingCartRepository groceryShoppingCartRepository;
    @Mock
    private GroceryService groceryService;
    @InjectMocks
    private ShoppingCartService shoppingCartService;
    private MockHttpServletRequest httpRequest;
    @Mock
    private UnitRepository unitRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        httpRequest = new MockHttpServletRequest();
    }

    @Test
    @DisplayName("Does createShoppingCart return the id to the shopping cart connected to the shopping list if it already is created")
    void is_the_id_returned_when_create_shoppingList_is_called_for_a_refrigerator_when_shopping_list_already_is_created() throws ShoppingListNotFound {
        long refrigeratorId = 1L;
        String refrigeratorName = "test";
        String refrigeratorAddress = "ntnu";
        Refrigerator refrigerator = Refrigerator.builder()
                .id(refrigeratorId)
                .name(refrigeratorName)
                .address(refrigeratorAddress)
                .build();
        ShoppingList shoppingList = ShoppingList.builder()
                .id(1L)
                .refrigerator(refrigerator)
                .build();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                        .id(1L)
                        .shoppingList(shoppingList)
                        .build();

        when(shoppingListRepository.findById(shoppingList.getId())).thenReturn(Optional.of(shoppingList));
        when(shoppingCartRepository.findByShoppingListId(shoppingList.getId())).thenReturn(Optional.ofNullable(shoppingCart));

        long resultId = shoppingCartService.createShoppingCart(shoppingList.getId());

        assert shoppingCart != null;
        assertEquals(resultId, shoppingCart.getId());
    }

    @Test
    @DisplayName("Is it possible to create a shopping cart if it does not already exits for the shopping list")
    void possible_to_create_new_shoppingCart_for_shoppingList() throws ShoppingListNotFound {
        long refrigeratorId = 1L;
        String refrigeratorName = "test";
        String refrigeratorAddress = "ntnu";
        Refrigerator refrigerator = Refrigerator.builder()
                .id(refrigeratorId)
                .name(refrigeratorName)
                .address(refrigeratorAddress)
                .build();
        ShoppingList shoppingList = ShoppingList.builder()
                .id(1L)
                .refrigerator(refrigerator)
                .build();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .shoppingList(shoppingList)
                .build();

        when(shoppingListRepository.findById(shoppingList.getId())).thenReturn(Optional.of(shoppingList));
        when(shoppingCartRepository.findByShoppingListId(shoppingList.getId())).thenReturn(Optional.empty());
        when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);

        long resultId = shoppingCartService.createShoppingCart(shoppingList.getId());

        assertEquals(resultId, shoppingCart.getId());
    }

    @Test
    @DisplayName("Throws getGroceries NoGroceriesFound when it is not any groceries in the database")
    void throws_getGroceries_NoGroceriesFound_when_it_is_no_groceries_in_the_database() {
        long shoppingCartId = 1L;
        when(shoppingCartRepository.findByShoppingCartId(shoppingCartId)).thenReturn(new ArrayList<>());

        assertThrows(NoGroceriesFound.class, () -> shoppingCartService.getGroceries(shoppingCartId));
    }

    @Test
    @DisplayName("getGroceries returns all groceries in shopping cart when the cart has groceries")
    void getGroceries_returns_all_groceries_in_shoppingCart() throws NoGroceriesFound {
        long shoppingCartId = 1L;
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        ShoppingList shoppingList = ShoppingList.builder()
                .refrigerator(new Refrigerator())
                .build();

        List<GroceryShoppingCart> groceries = new ArrayList<>();


        groceries.add(GroceryShoppingCart.builder().grocery(grocery1)
                .shoppingCart(new ShoppingCart(1L, shoppingList)).quantity(1).unit(Unit.builder().id(1L).name("123").build()).build());
        when(shoppingCartRepository.findByShoppingCartId(shoppingCartId)).thenReturn(groceries);


        List<ShoppingCartElementDTO> result = shoppingCartService.getGroceries(shoppingCartId);


        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getGroceryId());
    }


    @DisplayName("saveGrocery saves a grocery item as a new item when it does not exists a correspondent grocery")
    @Test
    void saveGrocery_saves_a_grocery_item_as_a_new_item_when_it_does_not_exists_a_correspondent_grocery() throws UserNotFoundException, UnauthorizedException {
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        GroceryShoppingCart groceryShoppingCartItem = GroceryShoppingCart.builder()
                .id(1L)
                .grocery(grocery1)
                .quantity(2)
                .unit(Unit.builder().id(1L).name("dl").build())
                .shoppingCart(shoppingCart)
                .build();
        SaveGroceryRequest saveGroceryRequest = SaveGroceryRequest.builder()
                .groceryId(grocery1.getId())
                .quantity(1)
                .foreignKey(shoppingCart.getId())
                .unitDTO(new UnitDTO(Unit.builder().id(1L).name("dl").build()))
                .build();

        when(shoppingCartRepository.findById(shoppingCart.getId())).thenReturn(Optional.of(shoppingCart));
        when(groceryService.getGroceryById(grocery1.getId())).thenReturn(grocery1);
        when(groceryShoppingCartRepository.findByGroceryIdAndShoppingCartId(grocery1.getId(), shoppingCart.getId())).thenReturn(Optional.empty());
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.SUPERUSER);
        when(groceryShoppingCartRepository.save(groceryShoppingCartItem)).thenReturn(groceryShoppingCartItem);
        when(unitRepository.findById(anyLong())).thenReturn(Optional.ofNullable(Unit.builder().id(1L).name("dl").build()));
        assertDoesNotThrow(() -> {
            shoppingCartService.saveGrocery(saveGroceryRequest, httpRequest);
        });
    }

    @DisplayName("saveGrocery saves a grocery item as a new item when it does exists a correspondent grocery")
    @Test
    void saveGrocery_saves_a_grocery_item_as_a_new_item_when_it_does_exists_a_correspondent_grocery() throws UserNotFoundException, UnauthorizedException {
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        GroceryShoppingCart groceryShoppingCartItem = GroceryShoppingCart.builder()
                .id(1L)
                .grocery(grocery1)
                .quantity(2)
                .shoppingCart(shoppingCart)
                .build();
        SaveGroceryRequest saveGroceryRequest = SaveGroceryRequest.builder()
                .groceryId(grocery1.getId())
                .quantity(5)
                .foreignKey(shoppingList.getId())
                .build();

        when(shoppingCartRepository.findById(shoppingCart.getId())).thenReturn(Optional.of(shoppingCart));
        when(groceryService.getGroceryById(grocery1.getId())).thenReturn(grocery1);
        when(groceryShoppingCartRepository.findByGroceryIdAndShoppingCartId(grocery1.getId(), shoppingCart.getId())).thenReturn(Optional.of(groceryShoppingCartItem));
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.SUPERUSER);
        when(groceryShoppingCartRepository.save(groceryShoppingCartItem)).thenReturn(groceryShoppingCartItem);

        assertDoesNotThrow(() -> {
            shoppingCartService.saveGrocery(saveGroceryRequest, httpRequest);
        });
    }

    @Test
    @DisplayName("Throws deleteGrocery NoGroceriesFound when it is not any groceries with the given id in the database")
    void throws_deleteGrocery_NoGroceriesFound_when_it_is_no_groceries_with_the_given_id_in_the_database() {
        long groceryListId = 1L;
        when(groceryShoppingCartRepository.findById(groceryListId)).thenReturn(Optional.empty());

        assertThrows(NoGroceriesFound.class, () -> shoppingCartService.deleteGrocery(groceryListId, httpRequest));
    }

    @Test
    @DisplayName("Throws deleteGrocery UnauthorizedException when the user is not authorized to delete a grocery")
    void throws_deleteGrocery_UnauthorizedException_when_the_user_is_not_authorized_to_delete_a_grocery() throws UserNotFoundException, UnauthorizedException {
        long groceryListId = 1L;
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        GroceryShoppingCart groceryShoppingCart = GroceryShoppingCart.builder()
                .id(1L)
                .grocery(grocery1)
                .shoppingCart(shoppingCart)
                .quantity(1)
                .build();

        when(groceryShoppingCartRepository.findById(groceryListId)).thenReturn(Optional.of(groceryShoppingCart));
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.USER);

        assertThrows(UnauthorizedException.class, () -> shoppingCartService.deleteGrocery(groceryListId, httpRequest));
    }

    @Test
    @DisplayName("Possible to delete grocery when the user is authorized")
    void possible_to_delete_grocery_when_the_user_is_authorized() throws UserNotFoundException, UnauthorizedException {
        long groceryListId = 1L;
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        GroceryShoppingCart groceryShoppingCart = GroceryShoppingCart.builder()
                .id(1L)
                .grocery(grocery1)
                .shoppingCart(shoppingCart)
                .quantity(1)
                .build();

        when(groceryShoppingCartRepository.findById(groceryListId)).thenReturn(Optional.of(groceryShoppingCart));
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.SUPERUSER);

        assertDoesNotThrow(() -> {
            shoppingCartService.deleteGrocery(groceryListId, httpRequest);
        });
    }


    @Test
    @DisplayName("Throws transferGroceryToRefrigerator NoGroceriesFound when it is not any groceries with the given id in the database")
    void throws_transferGroceryToRefrigerator_NoGroceriesFound_when_it_is_no_groceries_with_the_given_id_in_the_database() {
        long shoppingCartItem = 1L;
        when(groceryShoppingCartRepository.findById(shoppingCartItem)).thenReturn(Optional.empty());

        assertThrows(NoGroceriesFound.class, () -> shoppingCartService.transferGroceryToRefrigerator(shoppingCartItem, httpRequest, new CreateRefrigeratorGroceryDTO()));
    }

    @Test
    @DisplayName("Possible to transferGroceryToRefrigerator when the user is authorized to transfer a grocery to cart")
    void possible_to_transferGroceryToRefrigerator_when_the_user_is_authorized_to_transfer_a_grocery_to_cart() throws UserNotFoundException, UnauthorizedException, SaveException, ShoppingCartNotFound {
        long groceryListId = 1L;
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        GroceryShoppingCart groceryShoppingCart = GroceryShoppingCart.builder()
                .id(groceryListId)
                .grocery(grocery1)
                .shoppingCart(shoppingCart)
                .quantity(1)
                .build();

        when(groceryShoppingCartRepository.findById(groceryListId)).thenReturn(Optional.of(groceryShoppingCart));
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.SUPERUSER);

        assertDoesNotThrow(() -> shoppingCartService.transferGroceryToRefrigerator(groceryListId, httpRequest, new CreateRefrigeratorGroceryDTO()));
    }
}
