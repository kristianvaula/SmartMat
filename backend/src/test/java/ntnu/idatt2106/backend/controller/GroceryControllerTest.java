package ntnu.idatt2106.backend.controller;

import jakarta.persistence.EntityNotFoundException;
import ntnu.idatt2106.backend.exceptions.*;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.SubCategory;
import ntnu.idatt2106.backend.model.Unit;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.dto.RefrigeratorGroceryDTO;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import ntnu.idatt2106.backend.repository.RefrigeratorGroceryRepository;
import ntnu.idatt2106.backend.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

class GroceryControllerTest {

    @Mock
    private GroceryService groceryService;

    @Mock
    private CookieService cookieService;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefrigeratorGroceryRepository refrigeratorGroceryRepository;

    @Mock
    private ShoppingListServiceTest shoppingListServiceTest;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ShoppingListService shoppingListService;

    @InjectMocks
    private GroceryController groceryController;

    private MockHttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        httpServletRequest = new MockHttpServletRequest();
    }

    @Test
    void getGroceriesByRefrigerator_validInput_returnsGroceries() throws UserNotFoundException, UnauthorizedException, RefrigeratorNotFoundException {
        // Arrange
        long refrigeratorId = 1L;
        RefrigeratorGroceryDTO groceryDTO = new RefrigeratorGroceryDTO();
        List<RefrigeratorGroceryDTO> expectedList = new ArrayList<>();
        expectedList.add(groceryDTO);
        when(groceryService.getGroceriesByRefrigerator(refrigeratorId, httpServletRequest)).thenReturn(expectedList);

        // Act
        ResponseEntity<List<RefrigeratorGroceryDTO>> responseEntity = groceryController.getGroceriesByRefrigerator(refrigeratorId, httpServletRequest);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(expectedList, responseEntity.getBody());
    }

    @Test
    void getGroceriesByRefrigerator_invalidRefrigeratorId_throwsRefrigeratorNotFoundException() throws UserNotFoundException, UnauthorizedException, RefrigeratorNotFoundException {
        // Arrange
        long refrigeratorId = 1L;
        when(groceryService.getGroceriesByRefrigerator(refrigeratorId, httpServletRequest)).thenThrow(new RefrigeratorNotFoundException("Refrigerator not found"));

        // Act and Assert
        Assertions.assertThrows(RefrigeratorNotFoundException.class, () -> {
            groceryController.getGroceriesByRefrigerator(refrigeratorId, httpServletRequest);
        });
    }

    @Test
    void removeRefrigeratorGrocery_validInput_removesGrocery() throws UserNotFoundException, UnauthorizedException, EntityNotFoundException, NotificationException, SaveException, ShoppingListNotFound, NoSuchElementException {
        // Arrange
        long refrigeratorGroceryId = 1L;
        RefrigeratorGrocery refrigeratorGrocery = new RefrigeratorGrocery();
        refrigeratorGrocery.setId(refrigeratorGroceryId);
        refrigeratorGrocery.setGrocery(new Grocery(1L, "Milk", 1, "Description", new SubCategory()));
        refrigeratorGrocery.setPhysicalExpireDate(LocalDate.now());
        refrigeratorGrocery.setRefrigerator(new Refrigerator(1, "test", "ntnu"));
        refrigeratorGrocery.setUnit(Unit.builder().id(1L).name("dl").build());
        when(groceryService.getRefrigeratorGroceryById(refrigeratorGroceryId)).thenReturn(refrigeratorGrocery);
        // Act
        ResponseEntity<SuccessResponse> responseEntity = groceryController.removeRefrigeratorGrocery(refrigeratorGroceryId, httpServletRequest);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals("Grocery removed successfully", responseEntity.getBody().getMessage());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
