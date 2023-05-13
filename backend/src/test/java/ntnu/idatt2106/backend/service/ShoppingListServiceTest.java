package ntnu.idatt2106.backend.service;

import ntnu.idatt2106.backend.exceptions.*;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.ShoppingList;
import ntnu.idatt2106.backend.model.SubCategory;
import ntnu.idatt2106.backend.model.Unit;
import ntnu.idatt2106.backend.model.category.Category;
import ntnu.idatt2106.backend.model.dto.UnitDTO;
import ntnu.idatt2106.backend.model.dto.shoppingCartElement.ShoppingCartElementDTO;
import ntnu.idatt2106.backend.model.dto.shoppingListElement.ShoppingListElementDTO;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingList;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorShoppingList;
import ntnu.idatt2106.backend.model.requests.SaveGroceryRequest;
import ntnu.idatt2106.backend.repository.GroceryShoppingListRepository;
import ntnu.idatt2106.backend.repository.RefrigeratorShoppingListRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ShoppingListServiceTest {
    @Mock
    private RefrigeratorService refrigeratorService;

    @Mock
    private ShoppingListRepository shoppingListRepository;

    @Mock
    private GroceryShoppingListRepository groceryShoppingListRepository;

    @Mock
    private RefrigeratorShoppingListRepository refrigeratorShoppingListRepository;
    @Mock
    private GroceryService groceryService;
    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private UnitRepository unitRepository;

    @InjectMocks
    private ShoppingListService shoppingListService;
    private MockHttpServletRequest httpRequest;

    @BeforeEach void setup() {
        MockitoAnnotations.openMocks(this);
        httpRequest = new MockHttpServletRequest();
    }

    @Test
    @DisplayName("Does createShoppingList return the id to the shopping list connected to the refrigerator if it already is created")
    void is_the_id_returned_when_create_shoppingList_is_called_for_a_refrigerator_when_shopping_list_already_is_created() throws RefrigeratorNotFoundException {
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

        when(refrigeratorService.getRefrigerator(refrigeratorId)).thenReturn(refrigerator);
        when(shoppingListRepository.findByRefrigeratorId(refrigeratorId)).thenReturn(Optional.ofNullable(shoppingList));

        long resultId = shoppingListService.createShoppingList(refrigeratorId);

        assert shoppingList != null;
        assertEquals(resultId, shoppingList.getId());
    }

    @Test
    @DisplayName("Is it possible to create a shopping list if it does not already exits for the refrigerator")
    void possible_to_create_new_shoppingList_for_refrigerator() throws RefrigeratorNotFoundException {
        long refrigeratorId = 1L;
        String refrigeratorName = "test";
        String refrigeratorAddress = "ntnu";
        Refrigerator refrigerator = Refrigerator.builder()
                        .id(refrigeratorId)
                        .name(refrigeratorName)
                        .address(refrigeratorAddress)
                        .build();
        ShoppingList shoppingList = ShoppingList.builder()
                        .refrigerator(refrigerator)
                        .build();

        when(refrigeratorService.getRefrigerator(refrigeratorId)).thenReturn(refrigerator);
        when(shoppingListRepository.findByRefrigeratorId(refrigeratorId)).thenReturn(Optional.empty());
        when(shoppingListRepository.save(shoppingList)).thenReturn(shoppingList);

        long resultId = shoppingListService.createShoppingList(refrigeratorId);

        assertEquals(resultId, shoppingList.getId());
    }

    @Test
    @DisplayName("Throws getGroceries NoGroceriesFound when it is not any groceries in the database and is requested is false")
    void throws_getGroceries_NoGroceriesFound_when_it_is_no_groceries_in_the_database_and_isRequested_is_false() {
        long shoppingListId = 1L;
        boolean isRequested = false;
        when(groceryShoppingListRepository.findByShoppingListId(shoppingListId, isRequested)).thenReturn(new ArrayList<>());

        assertThrows(NoGroceriesFound.class, () -> shoppingListService.getGroceries(shoppingListId, isRequested));
    }

    @Test
    @DisplayName("Throws getGroceries NoGroceriesFound when it is not any groceries in the database and is requested is true")
    void throws_getGroceries_NoGroceriesFound_when_it_is_no_groceries_in_the_database_and_isRequested_is_true() {
        long shoppingListId = 1L;
        boolean isRequested = true;
        when(groceryShoppingListRepository.findByShoppingListId(shoppingListId, isRequested)).thenReturn(new ArrayList<>());

        assertThrows(NoGroceriesFound.class, () -> shoppingListService.getGroceries(shoppingListId, isRequested));
    }

    @Test
    @DisplayName("getGroceries returns all approved groceries when is requested is false")
    void getGroceries_returns_all_approved_groceries_when_isRequested_is_false() throws NoGroceriesFound {
        long shoppingListId = 1L;
        boolean isRequested = true;
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();

        List<GroceryShoppingList> groceries = new ArrayList<>();


        groceries.add(GroceryShoppingList.builder().grocery(grocery1).unit(Unit.builder().id(1L).name("dl").build()).shoppingList(new ShoppingList()).quantity(1).isRequest(false).build());
        when(groceryShoppingListRepository.findByShoppingListId(shoppingListId, isRequested)).thenReturn(groceries);


        List<ShoppingListElementDTO> result = shoppingListService.getGroceries(shoppingListId, isRequested);


        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getGroceryId());
    }

    @Test
    @DisplayName("getGroceries returns all suggested groceries when is requested is true")
    void getGroceries_returns_all_suggested_groceries_when_isRequested_is_true() throws NoGroceriesFound {
        long shoppingListId = 1L;
        boolean isRequested = true;
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        Grocery grocery1 = Grocery.builder()
                .id(1L)
                .name("Apple")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();

        List<GroceryShoppingList> groceries = new ArrayList<>();


        groceries.add(GroceryShoppingList.builder().grocery(grocery1).unit(Unit.builder().id(1L).name("dl").build()).shoppingList(new ShoppingList()).quantity(1).isRequest(true).build());
        when(groceryShoppingListRepository.findByShoppingListId(shoppingListId, isRequested)).thenReturn(groceries);

        List<ShoppingListElementDTO> result = shoppingListService.getGroceries(shoppingListId, isRequested);


        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getGroceryId());
    }

    @Test
    @DisplayName("Throws getGroceries with specified category NoGroceriesFound when it is not any groceries in the database and is requested is false")
    void throws_getGroceries_with_specified_category_NoGroceriesFound_when_no_groceries_in_the_database_and_isRequested_is_false() {
        long shoppingListId = 1L;
        boolean isRequested = false;
        long categoryId = 1L;
        when(groceryShoppingListRepository.findByShoppingListIdAndCategoryId(shoppingListId, categoryId, isRequested)).thenReturn(new ArrayList<>());

        assertThrows(NoGroceriesFound.class, () -> shoppingListService.getGroceries(shoppingListId, isRequested));
    }

    @Test
    @DisplayName("Throws getGroceries with specified category NoGroceriesFound when it is not any groceries in the database and is requested is true")
    void throws_getGroceries_with_specified_category_NoGroceriesFound_when_no_groceries_in_the_database_and_isRequested_is_true() {
        long shoppingListId = 1L;
        boolean isRequested = true;
        long categoryId = 1L;
        when(groceryShoppingListRepository.findByShoppingListIdAndCategoryId(shoppingListId, categoryId, isRequested)).thenReturn(new ArrayList<>());

        assertThrows(NoGroceriesFound.class, () -> shoppingListService.getGroceries(shoppingListId, isRequested));
    }

    @Test
    @DisplayName("getGroceries with specified category returns all approved groceries when is requested is false")
    void getGroceries_with_specified_category_returns_all_approved_groceries_when_isRequested_is_false() throws NoGroceriesFound {
        long shoppingListId = 1L;
        boolean isRequested = true;
        SubCategory subCategory = new SubCategory();
        Category category = Category.builder()
                        .id(1L)
                        .name("Test category")
                        .build();
        subCategory.setCategory(category);

        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();

        List<GroceryShoppingList> groceries = new ArrayList<>();


        groceries.add(GroceryShoppingList.builder().grocery(grocery1).unit(Unit.builder().id(1L).name("dl").build()).shoppingList(new ShoppingList()).quantity(1).isRequest(false).build());
        when(groceryShoppingListRepository.findByShoppingListIdAndCategoryId(shoppingListId, category.getId(), isRequested)).thenReturn(groceries);


        List<ShoppingListElementDTO> result = shoppingListService.getGroceries(shoppingListId, category.getId(), isRequested);


        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getGroceryId());
        assertEquals("Test category", result.get(0).getCategoryName());

    }

    @Test
    @DisplayName("getGroceries with specified category returns all suggested groceries when is requested is true")
    void getGroceries_with_specified_category_returns_all_suggested_groceries_when_isRequested_is_true() throws NoGroceriesFound {
        long shoppingListId = 1L;
        boolean isRequested = true;
        SubCategory subCategory = new SubCategory();
        Category category = Category.builder()
                .id(1L)
                .name("Test category")
                .build();
        subCategory.setCategory(category);

        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();

        List<GroceryShoppingList> groceries = new ArrayList<>();


        groceries.add(GroceryShoppingList.builder().grocery(grocery1).unit(Unit.builder().id(1L).name("dl").build()).shoppingList(new ShoppingList()).quantity(1).isRequest(false).build());
        when(groceryShoppingListRepository.findByShoppingListIdAndCategoryId(shoppingListId, category.getId(), isRequested)).thenReturn(groceries);


        List<ShoppingListElementDTO> result = shoppingListService.getGroceries(shoppingListId, category.getId(), isRequested);


        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getGroceryId());
        assertEquals("Test category", result.get(0).getCategoryName());
    }

    @Test
    @DisplayName("Throws NoGroceriesFound when getGroceriesFromRefrigeratorShoppingList is called and there are not any groceries in the database")
    void throws_getGroceriesFromRefrigeratorShoppingList_NoGroceriesFound_when_it_is_no_groceries_in_the_database() {
        long shoppingListId = 1L;
        when(refrigeratorShoppingListRepository.findByShoppingListId(shoppingListId)).thenReturn(new ArrayList<>());

        assertThrows(NoGroceriesFound.class, () -> shoppingListService.getGroceriesFromRefrigeratorShoppingList(shoppingListId));
    }

    @Test
    @DisplayName("getGroceriesFromRefrigeratorShoppingList from shopping list returns all suggested groceries from refrigerator")
    void getGroceriesFromRefrigeratorShoppingList_from_shopping_list_returns_all_suggested_groceries_from_refrigerator() throws NoGroceriesFound {
        long shoppingListId = 1L;
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());

        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();

        List<RefrigeratorShoppingList> groceries = new ArrayList<>();


        groceries.add(RefrigeratorShoppingList.builder().grocery(grocery1).unit(Unit.builder().id(1L).name("dl").build()).shoppingList(new ShoppingList()).quantity(1).build());
        when(refrigeratorShoppingListRepository.findByShoppingListId(shoppingListId)).thenReturn(groceries);


        List<ShoppingCartElementDTO> result = shoppingListService.getGroceriesFromRefrigeratorShoppingList(shoppingListId);


        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getGroceryId());
    }

    @Test
    @DisplayName("Throws CategoryNotFound when getCategories is called and there are not any groceries in the database")
    void throws_getCategories_CategoryNotFound_when_it_is_no_groceries_in_the_database() {
        long shoppingListId = 1L;
        when(groceryShoppingListRepository.findCategoryByShoppingListId(shoppingListId)).thenReturn(new ArrayList<>());

        assertThrows(CategoryNotFound.class, () -> shoppingListService.getCategories(shoppingListId));
    }

    @Test
    @DisplayName("getCategories from shopping list returns all categories assosiated with the groceries from the shopping list")
    void getCategories_from_shopping_list_returns_all_categories_assosiated_with_groceries_from_shopping_list() throws CategoryNotFound {
        long shoppingListId = 1L;
        Category category = Category.builder()
                .id(1L)
                .name("Test category")
                .build();

        List<Category> categories = new ArrayList<>();

        categories.add(category);
        when(groceryShoppingListRepository.findCategoryByShoppingListId(shoppingListId)).thenReturn(categories);


        List<Category> result = shoppingListService.getCategories(shoppingListId);


        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    @DisplayName("editGrocery throws NoGroceriesFound when it does not exist any groceries with the given id")
    void editGrocery_throws_NoGroceriesFound_when_it_does_not_exist_any_groceries_with_the_given_id() {
        long groceryShoppingListId = 1L;
        int quantity = 2;
        when(groceryShoppingListRepository.findById(groceryShoppingListId)).thenReturn(Optional.empty());

        assertThrows(NoGroceriesFound.class, () -> shoppingListService.editGrocery(groceryShoppingListId, quantity, httpRequest));
    }

    @Test
    @DisplayName("editGrocery throws SaveException when the user is not authorized to edit the grocery")
    void editGrocery_throws_SaveException_when_the_user_is_not_authorized_to_Edit_the_grocery() throws UserNotFoundException, UnauthorizedException {
        long groceryShoppingListId = 1L;
        int quantity = 2;
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        GroceryShoppingList groceryShoppingListItem = GroceryShoppingList.builder()
                        .id(1L)
                        .shoppingList(shoppingList)
                        .grocery(grocery1)
                        .isRequest(false)
                        .quantity(2)
                        .build();


        when(groceryShoppingListRepository.findById(shoppingList.getId())).thenReturn(Optional.ofNullable(groceryShoppingListItem));
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.USER);

        assertThrows(SaveException.class, () -> shoppingListService.editGrocery(groceryShoppingListId, quantity, httpRequest));
    }

    @DisplayName("saveGrocery saves a grocery item as a new item when it does not exists a correspondent grocery")
    @ParameterizedTest(name = "when is requested is set to {0}")
    @ValueSource(booleans = {true, false})
    void saveGrocery_saves_a_grocery_item_as_a_new_item_when_it_does_not_exists_a_correspondent_grocery(boolean isRequested) throws UserNotFoundException, UnauthorizedException, SaveException, ShoppingListNotFound {
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        GroceryShoppingList groceryShoppingListItem = GroceryShoppingList.builder()
                .id(1L)
                .shoppingList(shoppingList)
                .grocery(grocery1)
                .isRequest(isRequested)
                .quantity(2)
                .build();
        SaveGroceryRequest saveGroceryRequest = SaveGroceryRequest.builder()
                        .groceryId(grocery1.getId())
                        .quantity(1)
                        .foreignKey(shoppingList.getId())
                        .unitDTO(new UnitDTO((Unit.builder().id(1L).name("dl").build())))
                        .build();

        when(shoppingListRepository.findById(shoppingList.getId())).thenReturn(Optional.of(shoppingList));
        when(groceryService.getGroceryById(grocery1.getId())).thenReturn(grocery1);
        when(groceryShoppingListRepository.findByGroceryIdAndShoppingListId(grocery1.getId(), shoppingList.getId())).thenReturn(Optional.empty());
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.USER);
        when(groceryShoppingListRepository.save(groceryShoppingListItem)).thenReturn(groceryShoppingListItem);
        when(unitRepository.findById(anyLong())).thenReturn(Optional.ofNullable(Unit.builder().id(1L).name("dl").build()));

        assertDoesNotThrow(() -> {
            shoppingListService.saveGrocery(saveGroceryRequest, httpRequest);
        });
    }

    @DisplayName("saveGrocery saves a grocery item as a new item when it does exists a correspondent grocery")
    @ParameterizedTest(name = "when is requested is set to {0}")
    @ValueSource(booleans = {true, false})
    void saveGrocery_saves_a_grocery_item_as_a_new_item_when_it_does_t_exists_a_correspondent_grocery(boolean isRequested) throws UserNotFoundException, UnauthorizedException, SaveException, ShoppingListNotFound {
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(new Category());
        Unit unit = Unit.builder()
                .id(3)
                .name("g")
                .weight(1)
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        GroceryShoppingList groceryShoppingListItem = GroceryShoppingList.builder()
                .id(1L)
                .unit(new Unit(3,"g",1))
                .shoppingList(shoppingList)
                .grocery(grocery1)
                .isRequest(isRequested)
                .quantity(2)
                .build();
        SaveGroceryRequest saveGroceryRequest = SaveGroceryRequest.builder()
                .groceryId(grocery1.getId())
                .unitDTO(new UnitDTO(new Unit(3,"g",1)))
                .quantity(5)
                .foreignKey(shoppingList.getId())
                .build();

        when(shoppingListRepository.findById(shoppingList.getId())).thenReturn(Optional.of(shoppingList));
        when(groceryShoppingListRepository.findByGroceryIdAndShoppingListId(grocery1.getId(), shoppingList.getId())).thenReturn(Optional.of(groceryShoppingListItem));
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.USER);
        when(groceryShoppingListRepository.save(groceryShoppingListItem)).thenReturn(groceryShoppingListItem);
        when(unitRepository.findById(any())).thenReturn(Optional.of(unit));

        assertDoesNotThrow(() -> {
            shoppingListService.saveGrocery(saveGroceryRequest, httpRequest);
        });
    }

    @DisplayName("saveGroceryToSuggestionForRefrigerator saves a grocery item as a new item when it does not exists a correspondent grocery")
    @Test
    void saveGroceryToSuggestionForRefrigerator_saves_a_grocery_item_as_a_new_item_when_it_does_not_exists_a_correspondent_grocery() throws UserNotFoundException, UnauthorizedException {
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        RefrigeratorShoppingList refrigeratorShoppingList = RefrigeratorShoppingList.builder()
                .id(1L)
                .shoppingList(shoppingList)
                .grocery(grocery1)
                .quantity(2)
                .unit(Unit.builder().id(1L).name("l").build())
                .build();

        when(shoppingListRepository.findByRefrigeratorId(shoppingList.getRefrigerator().getId())).thenReturn(Optional.of(shoppingList));
        when(refrigeratorShoppingListRepository.findByGroceryIdAndShoppingListId(grocery1.getId(), shoppingList.getId())).thenReturn(Optional.of(refrigeratorShoppingList));
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.SUPERUSER);
        when(refrigeratorShoppingListRepository.save(refrigeratorShoppingList)).thenReturn(refrigeratorShoppingList);

        assertDoesNotThrow(() -> {
            shoppingListService.saveGroceryToSuggestionForRefrigerator(grocery1.getId(), shoppingList.getRefrigerator().getId(), refrigeratorShoppingList.getUnit().getId(), refrigeratorShoppingList.getQuantity(), httpRequest);
        });
    }

    @DisplayName("saveGroceryToSuggestionForRefrigerator saves a grocery item as a new item when it does exists a correspondent grocery")
    @Test
    void saveGroceryToSuggestionForRefrigerator_saves_a_grocery_item_as_a_new_item_when_it_does_exists_a_correspondent_grocery() throws UserNotFoundException, UnauthorizedException {
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        RefrigeratorShoppingList refrigeratorShoppingList = RefrigeratorShoppingList.builder()
                .id(1L)
                .shoppingList(shoppingList)
                .grocery(grocery1)
                .quantity(2)
                .unit(Unit.builder().build())
                .build();

        when(shoppingListRepository.findByRefrigeratorId(shoppingList.getRefrigerator().getId())).thenReturn(Optional.of(shoppingList));
        when(refrigeratorShoppingListRepository.findByGroceryIdAndShoppingListId(grocery1.getId(), shoppingList.getId())).thenReturn(Optional.empty());
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.SUPERUSER);
        when(refrigeratorShoppingListRepository.save(refrigeratorShoppingList)).thenReturn(refrigeratorShoppingList);
        when(unitRepository.findById(anyLong())).thenReturn(Optional.ofNullable(Unit.builder().id(1L).build()));

        assertDoesNotThrow(() -> {
            shoppingListService.saveGroceryToSuggestionForRefrigerator(grocery1.getId(), shoppingList.getRefrigerator().getId(), refrigeratorShoppingList.getUnit().getId(), refrigeratorShoppingList.getQuantity(), httpRequest);
        });
    }

    @Test
    @DisplayName("Throws deleteGrocery NoGroceriesFound when it is not any groceries with the given id in the database")
    void throws_deleteGrocery_NoGroceriesFound_when_it_is_no_groceries_with_the_given_id_in_the_database() {
        long groceryListId = 1L;
        when(groceryShoppingListRepository.findById(groceryListId)).thenReturn(Optional.empty());

        assertThrows(NoGroceriesFound.class, () -> shoppingListService.deleteGrocery(groceryListId, httpRequest));
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        GroceryShoppingList groceryShoppingList = GroceryShoppingList.builder()
                        .id(1L)
                        .grocery(grocery1)
                        .shoppingList(shoppingList)
                        .quantity(1)
                        .isRequest(false)
                        .build();

        when(groceryShoppingListRepository.findById(groceryListId)).thenReturn(Optional.of(groceryShoppingList));
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.USER);

        assertThrows(UnauthorizedException.class, () -> shoppingListService.deleteGrocery(groceryListId, httpRequest));
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        GroceryShoppingList groceryShoppingList = GroceryShoppingList.builder()
                .id(1L)
                .grocery(grocery1)
                .shoppingList(shoppingList)
                .quantity(1)
                .isRequest(false)
                .build();

        when(groceryShoppingListRepository.findById(groceryListId)).thenReturn(Optional.of(groceryShoppingList));
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.SUPERUSER);

        assertDoesNotThrow(() -> {
            shoppingListService.deleteGrocery(groceryListId, httpRequest);
        });
    }

    @Test
    @DisplayName("Throws deleteRefrigeratorGrocery NoGroceriesFound when it is not any groceries with the given id in the database")
    void throws_deleteRefrigeratorGrocery_NoGroceriesFound_when_it_is_no_groceries_with_the_given_id_in_the_database() {
        long refrigeratorShoppingListId = 1L;
        when(refrigeratorShoppingListRepository.findById(refrigeratorShoppingListId)).thenReturn(Optional.empty());

        assertThrows(NoGroceriesFound.class, () -> shoppingListService.deleteGrocery(refrigeratorShoppingListId, httpRequest));
    }

    @Test
    @DisplayName("Throws deleteRefrigeratorGrocery UnauthorizedException when the user is not authorized to delete a grocery")
    void throws_deleteRefrigeratorGrocery_UnauthorizedException_when_the_user_is_not_authorized_to_delete_a_grocery() throws UserNotFoundException, UnauthorizedException {
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        RefrigeratorShoppingList refrigeratorShoppingList = RefrigeratorShoppingList.builder()
                .id(1L)
                .grocery(grocery1)
                .shoppingList(shoppingList)
                .quantity(1)
                .build();

        when(refrigeratorShoppingListRepository.findById(groceryListId)).thenReturn(Optional.of(refrigeratorShoppingList));
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.USER);

        assertThrows(UnauthorizedException.class, () -> shoppingListService.deleteRefrigeratorGrocery(groceryListId, httpRequest));
    }

    @Test
    @DisplayName("Possible to delete grocery from refrigerator shopping list when the user is authorized")
    void possible_to_delete_grocery_from_refrigerator_shopping_list_when_the_user_is_authorized() throws UserNotFoundException, UnauthorizedException {
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        RefrigeratorShoppingList refrigeratorShoppingList = RefrigeratorShoppingList.builder()
                .id(1L)
                .grocery(grocery1)
                .shoppingList(shoppingList)
                .quantity(1)
                .build();

        when(refrigeratorShoppingListRepository.findById(groceryListId)).thenReturn(Optional.of(refrigeratorShoppingList));
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.SUPERUSER);

        assertDoesNotThrow(() -> {
            shoppingListService.deleteRefrigeratorGrocery(groceryListId, httpRequest);
        });
    }

    @Test
    @DisplayName("Throws transferGroceryToCart NoGroceriesFound when it is not any groceries with the given id in the database")
    void throws_transferGroceryToCart_NoGroceriesFound_when_it_is_no_groceries_with_the_given_id_in_the_database() {
        long groceryShoppingListId = 1L;
        when(groceryShoppingListRepository.findById(groceryShoppingListId)).thenReturn(Optional.empty());

        assertThrows(NoGroceriesFound.class, () -> shoppingListService.transferGroceryToCart(groceryShoppingListId, httpRequest));
    }

    @Test
    @DisplayName("Throws transferGroceryToCart UnauthorizedException when the user is not authorized to transfer a grocery to cart")
    void throws_transferGroceryToCart_UnauthorizedException_when_the_user_is_not_authorized_to_transfer_a_grocery_to_cart() throws UserNotFoundException, UnauthorizedException {
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        GroceryShoppingList groceryShoppingListItem = GroceryShoppingList.builder()
                .id(groceryListId)
                .grocery(grocery1)
                .shoppingList(shoppingList)
                .quantity(1)
                .isRequest(false)
                .build();

        when(groceryShoppingListRepository.findById(groceryListId)).thenReturn(Optional.of(groceryShoppingListItem));
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.USER);

        assertThrows(UnauthorizedException.class, () -> shoppingListService.transferGroceryToCart(groceryListId, httpRequest));
    }

    @Test
    @DisplayName("Possible to transferGroceryToCart when the user is authorized to transfer a grocery to cart")
    void possible_to_transferGroceryToCart_when_the_user_is_authorized_to_transfer_a_grocery_to_cart() throws UserNotFoundException, UnauthorizedException, SaveException, ShoppingCartNotFound {
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        GroceryShoppingList groceryShoppingListItem = GroceryShoppingList.builder()
                .id(groceryListId)
                .grocery(grocery1)
                .shoppingList(shoppingList)
                .quantity(1)
                .unit(Unit.builder().id(1L).name("dl").build())
                .isRequest(false)
                .build();

        when(groceryShoppingListRepository.findById(groceryListId)).thenReturn(Optional.of(groceryShoppingListItem));
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.SUPERUSER);

        assertDoesNotThrow(() -> shoppingListService.transferGroceryToCart(groceryListId, httpRequest));
    }

    @Test
    @DisplayName("Throws transferRefrigeratorGroceryToCart NoGroceriesFound when it is not any groceries with the given id in the database")
    void throws_transferRefrigeratorGroceryToCart_NoGroceriesFound_when_it_is_no_groceries_with_the_given_id_in_the_database() {
        long refrigeratorShoppingListItemId = 1L;
        when(refrigeratorShoppingListRepository.findById(refrigeratorShoppingListItemId)).thenReturn(Optional.empty());

        assertThrows(NoGroceriesFound.class, () -> shoppingListService.transferRefrigeratorGroceryToCart(refrigeratorShoppingListItemId, httpRequest));
    }

    @Test
    @DisplayName("Throws transferRefrigeratorGroceryToCart UnauthorizedException when the user is not authorized to transfer a grocery to cart")
    void throws_transferRefrigeratorGroceryToCart_UnauthorizedException_when_the_user_is_not_authorized_to_transfer_a_grocery_to_cart() throws UserNotFoundException, UnauthorizedException {
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        RefrigeratorShoppingList refrigeratorShoppingList = RefrigeratorShoppingList.builder()
                .id(groceryListId)
                .grocery(grocery1)
                .shoppingList(shoppingList)
                .quantity(1)
                .build();

        when(refrigeratorShoppingListRepository.findById(groceryListId)).thenReturn(Optional.of(refrigeratorShoppingList));
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.USER);

        assertThrows(UnauthorizedException.class, () -> shoppingListService.transferRefrigeratorGroceryToCart(groceryListId, httpRequest));
    }

    @Test
    @DisplayName("Possible to transferRefrigeratorGroceryToCart when the user is authorized to transfer a grocery to cart")
    void possible_to_transferRefrigeratorGroceryToCart_when_the_user_is_authorized_to_transfer_a_grocery_to_cart() throws UserNotFoundException, UnauthorizedException, SaveException, ShoppingCartNotFound {
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
        Grocery grocery1 = Grocery.builder()
                .id(2L)
                .name("Banana")
                .groceryExpiryDays(1)
                .description("Description")
                .subCategory(subCategory)
                .build();
        RefrigeratorShoppingList refrigeratorShoppingList = RefrigeratorShoppingList.builder()
                .id(groceryListId)
                .grocery(grocery1)
                .shoppingList(shoppingList)
                .quantity(1)
                .unit(Unit.builder().id(1L).name("dl").build())
                .build();

        when(refrigeratorShoppingListRepository.findById(groceryListId)).thenReturn(Optional.of(refrigeratorShoppingList));
        when(groceryService.getFridgeRole(shoppingList.getRefrigerator(), httpRequest)).thenReturn(FridgeRole.SUPERUSER);

        assertDoesNotThrow(() -> shoppingListService.transferRefrigeratorGroceryToCart(groceryListId, httpRequest));
    }
}
