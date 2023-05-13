package ntnu.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.*;
import ntnu.idatt2106.backend.model.category.Category;
import ntnu.idatt2106.backend.model.dto.shoppingCartElement.ShoppingCartElementDTO;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingList;
import ntnu.idatt2106.backend.model.dto.shoppingListElement.ShoppingListElementDTO;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.model.requests.SaveGroceryRequest;
import ntnu.idatt2106.backend.service.ShoppingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for shopping list
 * The controller contains method to create a shopping list and operate on groceries in shopping list
 */
@RestController
@RequestMapping("/api/shopping-list")
@RequiredArgsConstructor
@Tag(name = "ShoppingList Controller", description = "Controller to handle the shopping list")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    Logger logger = LoggerFactory.getLogger(ShoppingListController.class);

    /**
     *
     * Creates a shopping list for the specified refrigerator.
     * @param refrigeratorId the id of the refrigerator for which to create the shopping list
     * @return a ResponseEntity containing the id of the newly created shopping list and an HTTP status code
     * @throws RefrigeratorNotFoundException if no refrigerator with the specified id is found
     */
    @Operation(summary = "Create a shopping list for a refrigerator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping list created successfully", content = @Content(schema = @Schema(type = "long"))),
            @ApiResponse(responseCode = "404", description = "Refrigerator not found"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create/{refrigeratorId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> createShoppingList(@PathVariable(name = "refrigeratorId") long refrigeratorId) throws RefrigeratorNotFoundException {
        logger.info("Received request to create shopping list for refrigerator with id {}", refrigeratorId);
        long shoppingListId = shoppingListService.createShoppingList(refrigeratorId);

        logger.info("Returning shopping list id {}", shoppingListId);
        return new ResponseEntity<>(shoppingListId, HttpStatus.OK);
    }

    /**
     * Retrieves the list of groceries from a shopping list with the specified id.
     * @param shoppingListId the id of the shopping list for which to retrieve the groceries
     * @return a ResponseEntity containing the list of groceries and an HTTP status code
     * @throws NoGroceriesFound if no groceries are found in the specified shopping list
     */
    @Operation(summary = "Get groceries from a shopping list by shopping list id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of groceries fetched successfully", content = @Content(schema = @Schema(implementation = ShoppingListElementDTO.class))),
            @ApiResponse(responseCode = "404", description = "No groceries found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/groceries/{shoppingListId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ShoppingListElementDTO>> getGroceriesFromShoppingList(@PathVariable(name="shoppingListId") long shoppingListId) throws NoGroceriesFound {
        logger.info("Received request to get groceries from shopping list with id {}", shoppingListId);
        List<ShoppingListElementDTO> groceries = shoppingListService.getGroceries(shoppingListId, false);

        logger.info("Returns groceries and status OK");
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }

    /**
     * Retrieves the list of groceries with the specified category id from a shopping list with the specified id.
     * @param shoppingListId the id of the shopping list for which to retrieve the groceries
     * @param categoryId the id of the category for which to retrieve the groceries
     * @return a ResponseEntity containing the list of groceries with the specified category id and an HTTP status code
     * @throws NoGroceriesFound if no groceries with the specified category id are found in the specified shopping list
     */
    @Operation(summary = "Get groceries from categorized shopping list by shopping list and category id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groceries fetched successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShoppingListElementDTO.class)))),
            @ApiResponse(responseCode = "404", description = "No groceries found for the specified shopping list and category"),
            @ApiResponse(responseCode = "401", description = "User is not authorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/category/groceries/{shoppingListId}/{categoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ShoppingListElementDTO>> getGroceriesFromCategorizedShoppingList(@PathVariable(name="shoppingListId") long shoppingListId,
                                                                                                @PathVariable(name="categoryId") long categoryId) throws NoGroceriesFound {
        logger.info("Received request to get groceries with category id {} from shopping list with id {}", categoryId, shoppingListId);
        List<ShoppingListElementDTO> groceries = shoppingListService.getGroceries(shoppingListId, categoryId, false);

        logger.info("Returns groceries with category id {} and status OK", categoryId);
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }

    /**
     * Retrieves a list of suggested groceries from the refrigerator for a given shopping list ID.
     * @param shoppingListId ID of the shopping list for which suggested groceries are requested
     * @return a ResponseEntity object containing a list of suggested groceries and an HTTP status code
     * @throws NoGroceriesFound if no suggested groceries are found for the shopping list
     */
    @Operation(summary = "Get suggested groceries from refrigerator for a shopping list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of suggested groceries from refrigerator for the shopping list", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShoppingCartElementDTO.class)))),
            @ApiResponse(responseCode = "404", description = "No suggested groceries found for the shopping list"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated or authorized to access the resource"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/suggested-refrigerator/groceries/{shoppingListId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ShoppingCartElementDTO>> getSuggestedGroceriesFromRefrigerator(@PathVariable(name="shoppingListId") long shoppingListId) throws NoGroceriesFound {
        logger.info("Received request to get suggested groceries from refrigerator for shopping list with id {}", shoppingListId);
        List<ShoppingCartElementDTO> groceries = shoppingListService.getGroceriesFromRefrigeratorShoppingList(shoppingListId);

        logger.info("Returns groceries and status OK");
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }

    /**
     * Retrieves a list of categories for a given shopping list ID.
     * @param shoppingListId ID of the shopping list for which categories are requested
     * @return a ResponseEntity object containing a list of categories and an HTTP status code
     * @throws CategoryNotFound if the requested shopping list or category was not found
     */
    @Operation(summary = "Get categories from a shopping list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Category.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated or authorized to access the resource"),
            @ApiResponse(responseCode = "404", description = "The requested shopping list or category was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/categories/{shoppingListId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Category>> getCategoriesFromShoppingList(@PathVariable(name="shoppingListId") long shoppingListId) throws CategoryNotFound {
        logger.info("Received request to get categories from shopping list with id {}", shoppingListId);
        List<Category> categories = shoppingListService.getCategories(shoppingListId);

        logger.info("Returns categories and status OK");
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }


    /**
     * Saves a grocery to a shopping list.
     * @param saveGroceryRequest the request containing the grocery and shopping list IDs
     * @param request the HTTP servlet request
     * @return a ResponseEntity with a SuccessResponse object indicating that the grocery was added successfully
     * @throws SaveException if an error occurs while saving the grocery to the shopping list
     * @throws UserNotFoundException if the user making the request is not found
     * @throws ShoppingListNotFound if the shopping list specified in the request is not found
     * @throws UnauthorizedException if the user making the request is not authorized to access the shopping list
     * @throws NoSuchElementException if the grocery specified in the request is not found
     */
    @Operation(summary = "Save a grocery to a shopping list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The grocery was added successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated or authorized to access the resource"),
            @ApiResponse(responseCode = "404", description = "The requested shopping list or grocery was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/add-grocery")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse> saveGroceryToShoppingList(@RequestBody SaveGroceryRequest saveGroceryRequest,
                                                                     HttpServletRequest request) throws SaveException, UserNotFoundException, ShoppingListNotFound, UnauthorizedException, NoSuchElementException {
        logger.info("Received request to save grocery with id {} to shopping list with id {}", saveGroceryRequest.getGroceryId(), saveGroceryRequest.getForeignKey());
        shoppingListService.saveGrocery(saveGroceryRequest, request);
        return new ResponseEntity<>(new SuccessResponse("The grocery was added successfully", HttpStatus.OK.value()), HttpStatus.OK);
    }

    /**
     * Edits a grocery in a shopping list.
     * @param groceryShoppingListId the ID of the grocery to edit
     * @param quantity the new quantity of the grocery
     * @param httpRequest the HTTP servlet request
     * @return a ResponseEntity with the edited GroceryShoppingList object
     * @throws SaveException if an error occurs while saving the edited grocery to the shopping list
     * @throws NoGroceriesFound if the grocery specified by the ID is not found in the shopping list
     * @throws UserNotFoundException if the user making the request is not found
     * @throws UnauthorizedException if the user making the request is not authorized to access the shopping list
     */
    @Operation(summary = "Edit a grocery in a shopping list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The grocery was edited successfully", content = @Content(schema = @Schema(implementation = GroceryShoppingList.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated or authorized to access the resource"),
            @ApiResponse(responseCode = "404", description = "The requested shopping list or grocery was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/edit-grocery/{groceryShoppingListId}/{quantity}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GroceryShoppingList> editGrocery(@PathVariable("groceryShoppingListId") long groceryShoppingListId,
                                                                   @PathVariable("quantity") int quantity,
                                                                   HttpServletRequest httpRequest) throws SaveException, NoGroceriesFound, UserNotFoundException, UnauthorizedException {
        logger.info("Received request to edit grocery item with id {}", groceryShoppingListId);
        GroceryShoppingList grocery = shoppingListService.editGrocery(groceryShoppingListId, quantity, httpRequest);
        return new ResponseEntity<>(grocery, HttpStatus.OK);
    }


    /**
     * Edit a grocery in the refrigerator shopping list.
     * @param groceryRefrigeratorShoppingListId the ID of the grocery in the refrigerator shopping list to edit.
     * @param quantity the new quantity of the grocery item.
     * @param httpRequest the HTTP request.
     * @return ResponseEntity containing SuccessResponse with status 200 if the grocery was edited successfully.
     * @throws SaveException if an error occurs while saving the grocery.
     * @throws NoGroceriesFound if the grocery is not found in the shopping list.
     * @throws UserNotFoundException if the user associated with the request is not found.
     * @throws UnauthorizedException if the user is not authorized to access the resource.
     * @throws ShoppingListNotFound if the shopping list is not found.
     * @throws NoSuchElementException if the grocery list ID is not found.
     */
    @Operation(summary = "Edit a grocery in the refrigerator shopping list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The grocery was edited successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated or authorized to access the resource"),
            @ApiResponse(responseCode = "404", description = "The requested grocery or shopping list was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/edit-refrigerator-grocery/{groceryRefrigeratorShoppingListId}/{quantity}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse> editRefrigeratorGrocery(@PathVariable("groceryRefrigeratorShoppingListId") long groceryRefrigeratorShoppingListId,
                                                                   @PathVariable("quantity") int quantity,
                                                                   HttpServletRequest httpRequest) throws SaveException, NoGroceriesFound, UserNotFoundException, UnauthorizedException, ShoppingListNotFound, NoSuchElementException {
        logger.info("Received request to edit grocery item with id {}", groceryRefrigeratorShoppingListId);
        shoppingListService.editRefrigeratorGrocery(groceryRefrigeratorShoppingListId, quantity, httpRequest);
        return new ResponseEntity<>(new SuccessResponse("The grocery was edited successfully", HttpStatus.OK.value()), HttpStatus.OK);
    }

    /**
     * Remove a grocery from a shopping list.
     * @param groceryListId the ID of the grocery to remove from the shopping list.
     * @param httpRequest the HTTP request.
     * @return ResponseEntity containing Boolean with status 200 if the grocery was removed successfully.
     * @throws UnauthorizedException if the user is not authorized to access the resource.
     * @throws NoGroceriesFound if the grocery is not found in the shopping list.
     * @throws UserNotFoundException if the user associated with the request is not found.
     */
    @Operation(summary = "Remove a grocery from a shopping list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The grocery was removed successfully", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated or authorized to access the resource"),
            @ApiResponse(responseCode = "404", description = "The requested grocery was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete-grocery/{groceryListId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> removeGroceryFromShoppingList(@PathVariable(name="groceryListId") long groceryListId, HttpServletRequest httpRequest) throws UnauthorizedException, NoGroceriesFound, UserNotFoundException {
        logger.info("Received request to delete grocery list item with id {}", groceryListId);
        shoppingListService.deleteGrocery(groceryListId, httpRequest); //throws error if the deletion was unsuccessful

        logger.info("Returns deleteStatus and status OK");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    /**
     * Delete a refrigerator grocery from a shopping list.
     * @param refrigeratorShoppingListId The id of the refrigerator grocery to be deleted.
     * @param httpRequest The HttpServletRequest of the request.
     * @return A ResponseEntity<Boolean> indicating whether the deletion was successful.
     * @throws UnauthorizedException If the user is not authenticated or authorized to access the resource.
     * @throws NoGroceriesFound If no groceries are found in the shopping list.
     * @throws UserNotFoundException If the user is not found.
     */
    @Operation(summary = "Delete a refrigerator grocery from a shopping list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The grocery was deleted successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated or authorized to access the resource"),
            @ApiResponse(responseCode = "404", description = "The requested grocery was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete-refrigerator-grocery/{refrigeratorShoppingListId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> removeRefrigeratorGroceryFromShoppingList(@PathVariable(name="refrigeratorShoppingListId") long refrigeratorShoppingListId, HttpServletRequest httpRequest) throws UnauthorizedException, NoGroceriesFound, UserNotFoundException {
        logger.info("Received request to delete refrigerator grocery list item f with id {}", refrigeratorShoppingListId);
        shoppingListService.deleteRefrigeratorGrocery(refrigeratorShoppingListId, httpRequest); //throws error if the deletion was unsuccessful

        logger.info("Returns deleteStatus and status OK");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    /**
     * Get requested groceries from a shopping list.
     * @param shoppingListId The id of the shopping list.
     * @return A ResponseEntity<List<ShoppingListElementDTO>> containing the requested groceries.
     * @throws NoGroceriesFound If no groceries are found in the shopping list.
     */
    @Operation(summary = "Get requested groceries from a shopping list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested groceries retrieved successfully", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "401", description = "User is not authenticated or authorized to access the resource"),
            @ApiResponse(responseCode = "404", description = "The requested shopping list was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("requested/groceries/{shoppingListId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ShoppingListElementDTO>> getRequestedGroceries(@PathVariable("shoppingListId") long shoppingListId) throws NoGroceriesFound {
        logger.info("Received request to get groceries requested to the shopping list with id {}", shoppingListId);
        List<ShoppingListElementDTO> groceries = shoppingListService.getGroceries(shoppingListId, true);

        logger.info("Returns groceries and status OK");
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }

    /**
     * Retrieves a list of requested groceries from a categorized shopping list with the specified shopping list ID and category ID.
     * @param shoppingListId the ID of the categorized shopping list to retrieve the requested groceries from
     * @param categoryId the ID of the category that the requested groceries belong to
     * @return a ResponseEntity containing a list of ShoppingListElementDTO objects representing the requested groceries, and an HTTP status code indicating success or failure
     * @throws NoGroceriesFound if no requested groceries were found for the specified shopping list and category
     * @throws IllegalArgumentException if the request is invalid
     */
    @Operation(summary = "Get requested groceries from a categorized shopping list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of requested groceries from the categorized shopping list is returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShoppingListElementDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated or authorized to access the resource"),
            @ApiResponse(responseCode = "404", description = "The requested groceries were not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/requested/groceries/{shoppingListId}/{categoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ShoppingListElementDTO>> getRequestedGroceriesFromCategorizedShoppingList(@PathVariable("shoppingListId") long shoppingListId,
                                                                                                         @PathVariable("categoryId") long categoryId) throws NoGroceriesFound {
        logger.info("Received request to get groceries requested to the shopping list with id {}", shoppingListId);
        List<ShoppingListElementDTO> groceries = shoppingListService.getGroceries(shoppingListId, categoryId, true);

        logger.info("Returns groceries and status OK");
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }

    /**
     * Transfers a grocery item with the specified grocery shopping list ID from a shopping list to the shopping cart.
     * @param groceryShoppingListId the ID of the grocery item in the shopping list to transfer to the shopping cart
     * @param httpRequest the HTTP request containing the user's authentication and authorization information
     * @return a ResponseEntity containing a Boolean value indicating whether the transfer was successful, and an HTTP status code indicating success or failure
     * @throws UnauthorizedException if the user is not authenticated or authorized to access the resource
     * @throws NoGroceriesFound if the specified grocery item was not found in the shopping list
     * @throws UserNotFoundException if the user could not be found
     * @throws ShoppingCartNotFound if the shopping cart could not be found
     * @throws SaveException if there was an error saving the changes to the database
     * @throws NoSuchElementException if the specified grocery item or shopping cart was not found
     */
    @Operation(summary = "Transfer a grocery item from a shopping list to the shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The grocery was transferred successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated or authorized to access the resource"),
            @ApiResponse(responseCode = "404", description = "The requested grocery or shopping cart was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("transfer-shopping-cart/{groceryShoppingListId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> transferToShoppingCart(@PathVariable("groceryShoppingListId") long groceryShoppingListId, HttpServletRequest httpRequest) throws UnauthorizedException, NoGroceriesFound, UserNotFoundException, ShoppingCartNotFound, SaveException, NoSuchElementException {
        logger.info("Received request to transfer grocery item with id {} in shopping list to shopping cart", groceryShoppingListId);

        shoppingListService.transferGroceryToCart(groceryShoppingListId, httpRequest); //throws error if the transfer was unsuccessful
        logger.info("Returns transferStatus and status OK");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    /**
     * This method transfers a refrigerator grocery item to the shopping cart for the authenticated user.
     * @param refrigeratorShoppingListId the ID of the grocery item in the refrigerator shopping list to be transferred to the cart.
     * @param httpRequest the HTTP request object containing the authentication token of the user making the request.   @return ResponseEntity<Boolean> the response entity containing a boolean indicating whether the transfer was successful, and an HTTP status code.
     * @throws UnauthorizedException if the user is not authenticated or authorized to access the resource.
     * @throws NoGroceriesFound if the requested grocery item was not found in the refrigerator shopping list.
     * @throws UserNotFoundException if the user making the request is not found.
     * @throws ShoppingCartNotFound if the shopping cart for the authenticated user is not found.
     * @throws SaveException if an error occurs while saving the transfer.
     * @throws NoSuchElementException if the requested grocery item does not exist.
     */
    @Operation(summary = "Transfer a refrigerator grocery to the shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The grocery was transferred successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated or authorized to access the resource"),
            @ApiResponse(responseCode = "404", description = "The requested grocery was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("refrigerator/transfer-shopping-cart/{refrigeratorShoppingListId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> transferRefrigeratorGroceryToShoppingCart(@PathVariable("refrigeratorShoppingListId") long refrigeratorShoppingListId, HttpServletRequest httpRequest) throws UnauthorizedException, NoGroceriesFound, UserNotFoundException, ShoppingCartNotFound, SaveException, NoSuchElementException {
        logger.info("Received request to transfer grocery item with id {} in refrigerator shopping list to shopping list", refrigeratorShoppingListId);
        shoppingListService.transferRefrigeratorGroceryToCart(refrigeratorShoppingListId, httpRequest); //throws error if the transfer was unsuccessful

        logger.info("Returns transferStatus and status OK");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
