package ntnu.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.*;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.dto.*;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.dto.response.ErrorResponse;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.model.recipe.Recipe;
import ntnu.idatt2106.backend.model.recipe.RecipeGrocery;
import ntnu.idatt2106.backend.model.requests.SaveGroceryListRequest;
import ntnu.idatt2106.backend.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ntnu.idatt2106.backend.exceptions.UnauthorizedException;
import ntnu.idatt2106.backend.service.GroceryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller regarding the groceries inside a refrigerator
 */
@RestController
@RequestMapping("/api/refrigerator/grocery")
@RequiredArgsConstructor
@Tag(name = "Grocery Controller", description = "Controller to handle the groceries in a refrigerator")
public class GroceryController {

    private final GroceryService groceryService;
    private final RefrigeratorService refrigeratorService;
    private final CookieService cookieService;
    private final UserService userService;
    private final JwtService jwtService;
    private final NotificationService notificationService;
    private final ShoppingListService shoppingListService;
    private final RecipeService recipeService;
    Logger logger = LoggerFactory.getLogger(GroceryController.class);

    /**
     * Getter for all groceries in the refrigerator given in the parameter
     * @param refrigeratorId ID to the refrigerator to retrieve groceries from
     * @param httpServletRequest http request
     * @return list of groceries
     * @throws UserNotFoundException If the user is not found
     * @throws UnauthorizedException If the user is not authorized
     * @throws RefrigeratorNotFoundException If the refrigerator is not found
     */
    @Operation(summary = "Get all groceries by refrigerator id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of groceries fetched successfully", content = @Content(schema = @Schema(implementation = RefrigeratorGroceryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Refrigerator not found"),
            @ApiResponse(responseCode = "401", description = "User is not authorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{refrigeratorId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RefrigeratorGroceryDTO>> getGroceriesByRefrigerator(@Valid @PathVariable long refrigeratorId, HttpServletRequest httpServletRequest) throws UserNotFoundException, UnauthorizedException, RefrigeratorNotFoundException {
        logger.info("Received request for groceries by refrigerator with id: {}", refrigeratorId);
        return new ResponseEntity<>(groceryService.getGroceriesByRefrigerator(refrigeratorId, httpServletRequest), HttpStatus.OK);
    }

    /**
     * Removes a refrigerator by grocery id
     * @param refrigeratorGroceryId ID to the refrigerator to remove
     * @param httpServletRequest http request
     * @return success response: string with message and status code
     * @throws UserNotFoundException If the user is not found
     * @throws UnauthorizedException If the user is unauthorized
     * @throws EntityNotFoundException If an entity was not found
     * @throws NotificationException If an error occurred related to notifications
     * @throws SaveException If an error occurred during saving
     * @throws ShoppingListNotFound If the shopping list was not found
     * @throws NoSuchElementException If it does not exist an element
     */
    @Operation(summary = "Remove a refrigerator grocery by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refrigerator grocery removed successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "Refrigerator grocery not found"),
            @ApiResponse(responseCode = "401", description = "User is unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/remove/{refrigeratorGroceryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse> removeRefrigeratorGrocery(@Valid @PathVariable long refrigeratorGroceryId, HttpServletRequest httpServletRequest) throws UserNotFoundException, UnauthorizedException, EntityNotFoundException, NotificationException, SaveException, ShoppingListNotFound, NoSuchElementException {
        logger.info("Received request to remove refrigeratorGrocery with id: {}",refrigeratorGroceryId);
        RefrigeratorGrocery refrigeratorGrocery = groceryService.getRefrigeratorGroceryById(refrigeratorGroceryId);
        notificationService.deleteNotificationsByRefrigeratorGrocery(refrigeratorGrocery);
        groceryService.removeRefrigeratorGrocery(refrigeratorGroceryId, httpServletRequest);
        shoppingListService.saveGroceryToSuggestionForRefrigerator(refrigeratorGrocery.getGrocery().getId(),
                refrigeratorGrocery.getRefrigerator().getId(), refrigeratorGrocery.getUnit().getId(), 1, httpServletRequest);
        return new ResponseEntity<>(new SuccessResponse("Grocery removed successfully", HttpStatus.OK.value()), HttpStatus.OK);
    }

    /**
     * "Eats" part of or entire refrigeratorGrocery. Calls the groceryService and passes entire DTO.
     * @param dto Contains the RefrigeratorGroceryDTO, UnitDTO and quantity
     * @param httpServletRequest request
     * @return returns a ResponseEntity.
     * @throws Exception
     */
    @Operation(summary = "eat parts or all of refrigeratorGrocery if user removes grocery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refrigerator grocery quantity updated successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "204", description = "Refrigerator grocery, user or shopping list not found"),
            @ApiResponse(responseCode = "401", description = "User is unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/eat")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> eatRefrigeratorGrocery(@RequestBody DeleteRefrigeratorGroceryDTO dto, HttpServletRequest httpServletRequest) throws Exception {
        try{
            logger.info("Received request to eat refrigeratorGrocery: " + dto.getRefrigeratorGroceryDTO().getId());
            RefrigeratorGrocery grocery = groceryService.eatRefrigeratorGrocery(dto, httpServletRequest);
            if(grocery != null){
                logger.info("All of grocery consumed, sending refrigeratorGrocery to shopping list");
                shoppingListService.saveGroceryToSuggestionForRefrigerator(grocery.getGrocery().getId(),
                        grocery.getRefrigerator().getId(), dto.getUnitDTO().getId(),dto.getQuantity(), httpServletRequest);
            }
        }catch(Exception e){
            throw new Exception(e);
        }
        return new ResponseEntity<>(new SuccessResponse("Grocery updated properly", HttpStatus.OK.value()), HttpStatus.OK);
    }

    /**
     * Trashes part of or entire refrigeratorGrocery. Calls the groceryService and passes entire DTO.
     * @param dto Contains the RefrigeratorGroceryDTO, UnitDTO and quantity
     * @param httpServletRequest request
     * @return returns a ResponseEntity.
     * @throws Exception
     */
    @Operation(summary = "Trashes parts or all of refrigeratorGrocery if user removes grocery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refrigerator grocery quantity removed successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "204", description = "Refrigerator grocery, user or shopping list not found"),
            @ApiResponse(responseCode = "401", description = "User is unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/trash")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> trashRefrigeratorGrocery(@RequestBody DeleteRefrigeratorGroceryDTO dto, HttpServletRequest httpServletRequest) throws Exception {
        try{
            logger.info("Received request to trash refrigeratorGrocery: " + dto.getRefrigeratorGroceryDTO().getId());
            RefrigeratorGrocery grocery = groceryService.trashRefrigeratorGrocery(dto, httpServletRequest);
            if(grocery != null){
                logger.info("All of grocery trashed, sending refrigeratorGrocery to shopping list");
                shoppingListService.saveGroceryToSuggestionForRefrigerator(grocery.getGrocery().getId(),
                        grocery.getRefrigerator().getId(),dto.getUnitDTO().getId(),dto.getQuantity(), httpServletRequest);
            }
        }catch(Exception e){
            throw new Exception(e);
        }
        return new ResponseEntity<>(new SuccessResponse("Grocery updated properly", HttpStatus.OK.value()), HttpStatus.OK);
    }

    /**
     * Removes part of or entire refrigeratorGrocery. Calls the groceryService and passes entire DTO.
     * @param dto Contains the RefrigeratorGroceryDTO, UnitDTO and quantity
     * @param httpServletRequest request
     * @return returns a ResponseEntity.
     * @throws Exception
     */
    @Operation(summary = "Remove parts or all of refrigeratorGrocery if user removes grocery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refrigerator grocery quantity removed successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "204", description = "Refrigerator grocery, user or shopping list not found"),
            @ApiResponse(responseCode = "401", description = "User is unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/remove")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> removeRefrigeratorGrocery(@RequestBody DeleteRefrigeratorGroceryDTO dto, HttpServletRequest httpServletRequest) throws Exception {
        try{
            logger.info("Received request to remove refrigeratorGrocery: " + dto.getRefrigeratorGroceryDTO().getId());
            RefrigeratorGrocery grocery = groceryService.useRefrigeratorGrocery(dto, httpServletRequest);
            if(grocery != null){
                logger.info("All of grocery removed, sending refrigeratorGrocery to shopping list");
                shoppingListService.saveGroceryToSuggestionForRefrigerator(grocery.getGrocery().getId(),
                        grocery.getRefrigerator().getId(),dto.getUnitDTO().getId(),dto.getQuantity(), httpServletRequest);
            }
        }catch(Exception e){
            throw new Exception(e);
        }
        return new ResponseEntity<>(new SuccessResponse("Grocery updated properly", HttpStatus.OK.value()), HttpStatus.OK);
    }

    /**
     * Create a new grocery item in a given refrigerator
     * @param request http request
     * @param dto grocery to create
     * @param refrigeratorId refrigerator id to create a new grocery in
     * @return response status and the created grocery
     * @throws Exception
     */
    @Operation(summary = "Create a new grocery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grocery created successfully", content = @Content(schema = @Schema(implementation = GroceryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/new/{refrigeratorId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createNewGrocery(HttpServletRequest request, @RequestBody CreateRefrigeratorGroceryDTO dto, @Valid @PathVariable long refrigeratorId) throws Exception {
        try{
            String jwt = cookieService.extractTokenFromCookie(request);
            User user = userService.findByEmail(jwtService.extractUsername(jwt)); // Pass the JWT token instead of the request
            logger.info("Received request to create grocery from user: "+ user.getEmail() + ".");
            List<GroceryDTO> DTOs = new ArrayList<>();
            DTOs.add(dto.getGroceryDTO());
            groceryService.addGrocery(new SaveGroceryListRequest(refrigeratorId, DTOs, dto.getUnitDTO(), dto.getQuantity()), request);
            return ResponseEntity.ok(dto.getGroceryDTO());
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    /**
     * Create a new grocery in the grocery entity in the database
     * @param grocery grocery to create
     * @return response status and the created grocery
     */
    @Operation(summary = "Create a new grocery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The grocery was created successfully", content = @Content(schema = @Schema(implementation = Grocery.class))),
            @ApiResponse(responseCode = "401", description = "User is not authenticated or authorized to access the resource", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Grocery> saveGrocery(@RequestBody GroceryDTO grocery) {
        Grocery savedGrocery = groceryService.createGrocery(grocery);
        return ResponseEntity.ok(savedGrocery);
    }

    /**
     * Method to get all groceries stored in the database
     * @return list of all groceries in the database
     * @throws NoGroceriesFound if not groceries was found
     */
    @Operation(summary = "Get all grocery DTOs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of grocery DTOs fetched successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = GroceryDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "204", description = "No groceries found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/allDTOs")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<GroceryDTO>> getAllGroceriesDTOs() throws NoGroceriesFound{
        logger.info("Received request to get all DTOs");
        List<GroceryDTO> groceries = groceryService.getAllGroceriesDTO();
        logger.info("Returning DTOs, and status kode OK");
        return new ResponseEntity<>(groceries, HttpStatus.OK);
    }

    /**
     * Update a grocery item in the user's refrigerator.
     *
     * @param request The HTTP request containing the JWT token in the cookie.
     * @param refrigeratorGroceryDTO The DTO containing the updated grocery item information.
     * @return The updated grocery item.
     * @throws Exception If there was an error updating the grocery item.
     */
    @Operation(summary = "Update a groceries attributes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated grocery successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = GroceryDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "204", description = "Grocery not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/updateGrocery")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateGrocery(HttpServletRequest request, @RequestBody RefrigeratorGroceryDTO refrigeratorGroceryDTO) throws Exception {
        try{
            logger.info("Received request to update grocery: " + refrigeratorGroceryDTO.getId());

            groceryService.updateRefrigeratorGrocery(refrigeratorGroceryDTO, request);

            logger.info("Successfully updated grocery:" +refrigeratorGroceryDTO.getId());
            return ResponseEntity.ok(refrigeratorGroceryDTO);
        }
        catch(Exception e){
            throw new Exception(e);
        }
    }

    /**
     * Getter for all ingredients in the refrigerator that matches a recipe
     * @param refrigeratorId ID to the refrigerator to retrieve ingredients from
     * @param recipeId ID to the recipe to get ingredients from
     * @return list of groceries needed for a recipe
     * @throws RefrigeratorNotFoundException If the refrigerator was not found
     */
    @Operation(summary = "Fetch a list of groceries in the refrigerator that matches a recipe ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groceries fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/matching-recipe/{refrigeratorId}/{recipeId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getIngredientsInRefrigerator(@Valid @PathVariable long refrigeratorId, @Valid @PathVariable long recipeId) throws RefrigeratorNotFoundException {
        logger.info("Received request for ingredients in refrigerator");
        Recipe recipe = recipeService.getRecipeById(recipeId);
        List<RecipeGrocery> recipeGroceries = recipeService.getIngredientsByRecipe(recipe);
        if(recipeGroceries.size() == 0) return new ResponseEntity<>(HttpStatus.OK);
        Refrigerator refrigerator = refrigeratorService.getRefrigerator(refrigeratorId);
        return new ResponseEntity<>(groceryService.getIngredientsInRefrigerator(recipeGroceries, refrigerator.getId()), HttpStatus.OK);
    }
}
