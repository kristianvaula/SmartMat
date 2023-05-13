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
import ntnu.idatt2106.backend.model.dto.CreateRefrigeratorGroceryDTO;
import ntnu.idatt2106.backend.model.dto.RefrigeratorGroceryDTO;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.model.dto.shoppingCartElement.ShoppingCartElementDTO;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import ntnu.idatt2106.backend.model.requests.SaveGroceryRequest;
import ntnu.idatt2106.backend.service.ShoppingCartService;
import ntnu.idatt2106.backend.service.ShoppingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing shopping carts and their contents
 */
@RestController
@RequestMapping("/api/shopping-cart")
@RequiredArgsConstructor
@Tag(name = "ShoppingCart Controller", description = "Controller to handle the shopping cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final ShoppingListService shoppingListService;
    private Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    /**
     * Endpoint for creating a new shopping cart for a given shopping list
     * @param shoppingListId the id of the shopping list to create the cart for
     * @return a ResponseEntity containing the id of the created shopping cart and a status of OK
     * @throws ShoppingListNotFound if the shopping list with the given id does not exist
     */
    @Operation(summary = "Create a shopping cart for a given shopping list ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping cart created successfully",
                    content = @Content(schema = @Schema(type = "long"))),
            @ApiResponse(responseCode = "404", description = "Shopping list not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create/{shoppingListId}")
    public ResponseEntity<Long> createShoppingCartId(@PathVariable(name = "shoppingListId") long shoppingListId) throws ShoppingListNotFound {
        logger.info("Received request to create shopping cart for shopping list with id {}", shoppingListId);
        long shoppingCartId = shoppingCartService.createShoppingCart(shoppingListId);

        logger.info("Returning shopping cart id {}", shoppingCartId);
        return new ResponseEntity<>(shoppingCartId, HttpStatus.OK);
    }

    /**
     * Retrieves all the groceries from the shopping cart with the given id.
     * @param shoppingCartId the id of the shopping cart to retrieve the groceries from
     * @return a ResponseEntity containing a List of ShoppingCartElementDTO objects representing the groceries retrieved from the shopping cart and the HTTP status
     * @throws NullPointerException if the shopping cart id is null
     * @throws NoGroceriesFound if no groceries were found in the shopping cart
     */
    @Operation(summary = "Get all groceries from shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of groceries fetched successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShoppingCartElementDTO.class)))),
            @ApiResponse(responseCode = "404", description = "No groceries found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/groceries/{shoppingCartId}")
    public ResponseEntity<List<ShoppingCartElementDTO>> getGroceriesFromShoppingCart(@PathVariable(name="shoppingCartId") long shoppingCartId) throws NullPointerException, NoGroceriesFound {
        logger.info("Received request to get groceries from shopping cart with id {}", shoppingCartId);
        List<ShoppingCartElementDTO> groceries = shoppingCartService.getGroceries(shoppingCartId);

        logger.info("Returns groceries and status OK");
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }

/**
 * Saves a grocery to a shopping cart.
 * @param groceryRequest the request object containing the id of the grocery to save and the id of the shopping cart to save it to
 * @param request the HTTP request object
 * @return a ResponseEntity containing a SuccessResponse object with a success message and the HTTP status
 * @throws UnauthorizedException if the user is not authorized to perform the operation
 * @throws ShoppingCartNotFound if the shopping cart with the specified id is not found
 * @throws UserNotFoundException if the user is not found
 * @throws SaveException if there is an error while saving the grocery
*/
 @Operation(summary = "Save a grocery to a shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grocery added successfully",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User is not authorized"),
            @ApiResponse(responseCode = "404", description = "Shopping cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/add-grocery")
    public ResponseEntity<SuccessResponse> saveGroceryToShoppingCart(@RequestBody SaveGroceryRequest groceryRequest, HttpServletRequest request) throws UnauthorizedException, ShoppingCartNotFound, UserNotFoundException, SaveException, NoSuchElementException {
        logger.info("Received request to save grocery with id {} to shopping cart with id {}", groceryRequest.getGroceryId(), groceryRequest.getForeignKey());
        shoppingCartService.saveGrocery(groceryRequest, request);
        return new ResponseEntity<>(new SuccessResponse("The grocery was added successfully", HttpStatus.OK.value()), HttpStatus.OK);
    }

    /**
     * This method transfers a grocery item from the shopping cart to the shopping list.
     * @param shoppingCartItemId the id of the grocery item in the shopping cart to be transferred.
     * @param httpRequest the HTTP request object.
     * @return ResponseEntity with a boolean indicating whether the transfer was successful and the HTTP status code.
     * @throws NoGroceriesFound if no groceries are found in the shopping cart.
     * @throws UserNotFoundException if the user associated with the shopping cart is not found.
     * @throws SaveException if there is an error while saving the grocery item to the shopping list.
     * @throws UnauthorizedException if the user is not authorized to perform the transfer.
     * @throws RefrigeratorNotFoundException if the refrigerator associated with the shopping cart is not found.
     * @throws ShoppingListNotFound if the shopping list associated with the user is not found.
     */
    @Operation(summary = "Transfer grocery from shopping cart to shopping list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grocery transferred successfully", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User is not authorized"),
            @ApiResponse(responseCode = "404", description = "Grocery or shopping cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/transfer-shoppingList/{shoppingCartItemId}")
    @Transactional(propagation =  Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResponseEntity<Boolean> transferToShoppingList(@PathVariable(name = "shoppingCartItemId") long shoppingCartItemId,
                                                          HttpServletRequest httpRequest) throws NoGroceriesFound, UserNotFoundException, SaveException, UnauthorizedException, RefrigeratorNotFoundException, ShoppingListNotFound, NoSuchElementException {
        logger.info("Received request to transfer grocery from shopping cart to shopping list");
        GroceryShoppingCart deletedGroceryItem = shoppingCartService.deleteGrocery(shoppingCartItemId, httpRequest);
        shoppingListService.saveGrocery(new SaveGroceryRequest(deletedGroceryItem), httpRequest);
        logger.info("Returns transferStatus and status OK");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    /**
     * Transfers a grocery item from the shopping cart to the refrigerator.
     * @param shoppingCartItemId the id of the grocery item to transfer from the shopping cart
     * @param dto the DTO containing the details of the grocery item to create in the refrigerator
     * @param httpRequest the HTTP request
     * @return a ResponseEntity<Boolean> indicating whether the transfer was successful or not
     * @throws NoGroceriesFound if no groceries are found in the shopping cart
     * @throws UserNotFoundException if the user making the request is not found
     * @throws SaveException if there is an error while saving the grocery item to the refrigerator
     * @throws UnauthorizedException if the user making the request is not authorized
     * @throws RefrigeratorNotFoundException if the refrigerator is not found
     */
    @Operation(summary = "Transfer grocery from shopping cart to refrigerator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grocery transferred successfully", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User is not authorized"),
            @ApiResponse(responseCode = "404", description = "Grocery or shopping cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/transfer-refrigerator/{shoppingCartItemId}")
    public ResponseEntity<Boolean> transferToRefrigerator(@PathVariable(name = "shoppingCartItemId") long shoppingCartItemId, @RequestBody CreateRefrigeratorGroceryDTO dto,
                                                             HttpServletRequest httpRequest) throws NoGroceriesFound, UserNotFoundException, SaveException, UnauthorizedException, RefrigeratorNotFoundException {
        logger.info("Received request to transfer grocery to refrigerator");
        shoppingCartService.transferGroceryToRefrigerator(shoppingCartItemId, httpRequest, dto);//throws error if the transfer was unsuccessful

        logger.info("Returns transferStatus and status OK");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }


    /**
     * Transfers all the grocery items from the shopping cart to the refrigerator.
     * @param request the array of SaveGroceryRequest objects containing the details of the grocery items to create in the refrigerator
     * @param httpRequest the HTTP request
     * @return a ResponseEntity<Boolean> indicating whether the transfer was successful or not
     * @throws NoGroceriesFound if no groceries are found in the shopping cart
     * @throws UserNotFoundException if the user making the request is not found
     * @throws SaveException if there is an error while saving the grocery items to the refrigerator
     * @throws UnauthorizedException if the user making the request is not authorized
     * @throws RefrigeratorNotFoundException if the refrigerator is not found
     */
    @Operation(summary = "Transfer all groceries to refrigerator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All groceries transferred successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "User is not authorized"),
            @ApiResponse(responseCode = "404", description = "No groceries found in the shopping cart"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/all/transfer-refrigerator")
    public ResponseEntity<Boolean> transferAllToRefrigerator(@RequestBody SaveGroceryRequest[] request, HttpServletRequest httpRequest) throws UserNotFoundException, NoGroceriesFound, SaveException, UnauthorizedException, RefrigeratorNotFoundException {
        logger.info("Received request to transfer groceries to refrigerator");
        shoppingCartService.transferAllGroceriesToRefrigerator(request, httpRequest);

        logger.info("Returns transferStatus and status OK");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
