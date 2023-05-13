package ntnu.idatt2106.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.*;
import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.model.dto.CreateRefrigeratorGroceryDTO;
import ntnu.idatt2106.backend.model.dto.GroceryDTO;
import ntnu.idatt2106.backend.model.dto.shoppingCartElement.ShoppingCartElementDTO;
import ntnu.idatt2106.backend.model.dto.shoppingCartElement.ShoppingCartElementDTOComparator;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import ntnu.idatt2106.backend.model.requests.SaveGroceryListRequest;
import ntnu.idatt2106.backend.model.requests.SaveGroceryRequest;
import ntnu.idatt2106.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The ShoppingCartService class provides methods to create a new shopping cart, or get an existing one,
 * get groceries in the shopping cart, and to delete, edit and transfer groceries to the refrigerator
 */
@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final GroceryShoppingCartRepository groceryShoppingCartRepository;
    private final GroceryService groceryService;
    private final UnitRepository unitRepository;

    private Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

    /**
     * Getter for the shopping list by the shopping list id
     * @param shoppingListId ID to the shopping list
     * @return Shopping list object
     * @throws ShoppingListNotFound If it is not find any shopping list for the shopping list id in the parameter
     */
    private ShoppingList getShoppingListById(long shoppingListId) throws ShoppingListNotFound {
        return shoppingListRepository.findById(shoppingListId)
                .orElseThrow(() -> new ShoppingListNotFound("Shopping list not found for shopping list id " + shoppingListId));
    }

    /**
     * Getter for the shopping list by the shopping list id
     * @param shoppingListId ID to the shopping list
     * @return Shopping list object
     * @throws ShoppingCartNotFound If it is not find any shopping cart for the shopping list id in the parameter
     */
    private ShoppingCart getShoppingCartByShoppingListId(long shoppingListId) throws ShoppingCartNotFound {
        return shoppingCartRepository.findByShoppingListId(shoppingListId)
                .orElseThrow(() -> new ShoppingCartNotFound("Shopping cart not found for shopping list id " + shoppingListId));
    }

    /**
     * Getter for the shopping list by the shopping cart id
     * @param shoppingCartId ID to the shopping cart
     * @return Shopping list object
     * @throws ShoppingCartNotFound If it is not find any shopping cart for the shopping list id in the parameter
     */
    private ShoppingCart getShoppingCartById(long shoppingCartId) throws ShoppingCartNotFound {
        return shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new ShoppingCartNotFound("Shopping cart not found for shopping cart id " + shoppingCartId));
    }

    /**
     * Creates a new shopping cart if it does not already exist a shopping cart for the refrigerator id
     * The shopping cart id to an already existing list is returned if it already exists a shopping cart for the given refrigerator
     * @param shoppingListId ID of connected shopping list
     * @return shopping cart id for the shopping list id in the parameter
     */
    public long createShoppingCart(long shoppingListId) throws ShoppingListNotFound {
        ShoppingList shoppingList = getShoppingListById(shoppingListId);

        try {
            ShoppingCart shoppingCart = getShoppingCartByShoppingListId(shoppingListId);
            return shoppingCart.getId();
        } catch (ShoppingCartNotFound e) {
            ShoppingCart newShoppingCart = new ShoppingCart();
            newShoppingCart.setShoppingList(shoppingList);

            shoppingCartRepository.save(newShoppingCart);
            logger.info("Created shopping cart with id {}", newShoppingCart.getId());
            return newShoppingCart.getId();
        }
    }

    /**
     * Getter for all groceries in the shopping cart specified in the parameter
     * @param shoppingCartId ID to the shopping cart to retrieve groceries from
     * @return All groceries from the shopping cart with the shopping cart id specified in the parameter
     * @exception NoGroceriesFound Could not find any groceries
     */
    public List<ShoppingCartElementDTO> getGroceries(long shoppingCartId) throws NoGroceriesFound {
        List<GroceryShoppingCart> groceries = shoppingCartRepository.findByShoppingCartId(shoppingCartId);
        if (groceries.isEmpty()) {
            logger.info("Received no groceries from the database");
            throw new NoGroceriesFound("Could not find any groceries for shopping cart id " + shoppingCartId);
        }
        return groceries.stream().map(ShoppingCartElementDTO::new).
                sorted(new ShoppingCartElementDTOComparator()).toList();
    }

    /**
     * Adds predefined grocery to the shopping cart for groceries
     * @param groceryRequest JSON object to save
     * @param httpRequest The http request
     * @exception ShoppingCartNotFound If the shopping cart is not found
     * @exception UserNotFoundException If the user is not found
     * @exception UnauthorizedException If the user is not authorized
     * @exception SaveException If error while saving
     */
    public void saveGrocery(SaveGroceryRequest groceryRequest, HttpServletRequest httpRequest) throws UnauthorizedException, ShoppingCartNotFound, UserNotFoundException, SaveException, NoSuchElementException {
        ShoppingCart shoppingCart = getShoppingCartById(groceryRequest.getForeignKey());
        FridgeRole fridgeRole = groceryService.getFridgeRole(shoppingCart.getShoppingList().getRefrigerator(), httpRequest);

        if (fridgeRole == FridgeRole.SUPERUSER) {
            Optional<GroceryShoppingCart> groceryShoppingCart = groceryShoppingCartRepository
                    .findByGroceryIdAndShoppingCartId(groceryRequest.getGroceryId(), shoppingCart.getId());

            if (groceryShoppingCart.isPresent()) {
                logger.info("Grocery item exist already in entity for shopping cart. Increment quantity with one");
                groceryShoppingCart.get().editQuantity(groceryRequest.getQuantity());
            } else {
                logger.info("Grocery item does not exit in shopping cart. Saving the grocery to the database");
                Grocery grocery = groceryService.getGroceryById(groceryRequest.getGroceryId());
                Unit unit = unitRepository.findById(groceryRequest.getUnitDTO().getId()).orElseThrow(() -> new NoSuchElementException("Could not find specified unit"));
                groceryShoppingCart = Optional.of(GroceryShoppingCart.builder()
                        .grocery(grocery)
                        .shoppingCart(shoppingCart)
                        .quantity(groceryRequest.getQuantity())
                        .unit(unit)
                        .build());
            }

            try {
                groceryShoppingCartRepository.save(groceryShoppingCart.get());
            } catch (Exception e) {
                logger.info("Error when saving grocery");
                throw new SaveException("Could not save the grocery to shopping cart");
            }
        }
    }

    /**
     * Deletes a grocery from the shopping cart
     * @param groceryListId ID to the grocery to delete
     * @param httpRequest http request
     * @return grocery item deleted
     * @throws UnauthorizedException If not authorized
     * @throws NoGroceriesFound If the grocery item not is found in the shopping list
     * @throws UserNotFoundException If the user is not found
     */
    public GroceryShoppingCart deleteGrocery(long groceryListId, HttpServletRequest httpRequest) throws UnauthorizedException, NoGroceriesFound, UserNotFoundException {
        GroceryShoppingCart groceryShoppingCart = groceryShoppingCartRepository.findById(groceryListId)
                .orElseThrow(() -> new NoGroceriesFound("Could not find grocery item"));
        FridgeRole fridgeRole = groceryService.getFridgeRole(groceryShoppingCart.getShoppingCart().getShoppingList().getRefrigerator(), httpRequest);

        if (fridgeRole == FridgeRole.SUPERUSER) {
            groceryShoppingCartRepository.deleteById(groceryListId);
            return groceryShoppingCart;
        } else {
            throw new UnauthorizedException("The user is not authorized to delete a grocery list item");
        }
    }

    /**
     * Transfers one grocery from the shopping cart to the refrigerator
     * @param shoppingCartItemId ID to the grocery in the shopping cart
     * @param httpRequest http request
     * @throws UserNotFoundException If the user is not found
     * @throws SaveException If there occurred an error while saving
     * @throws UnauthorizedException If not authorized
     * @throws RefrigeratorNotFoundException If no refrigerator was found
     * @throws NoGroceriesFound If no groceries was found in the shopping cart
     */
    @Transactional(propagation =  Propagation.REQUIRED, rollbackFor = Exception.class)
    public void transferGroceryToRefrigerator(long shoppingCartItemId, HttpServletRequest httpRequest, CreateRefrigeratorGroceryDTO dto) throws NoGroceriesFound, UserNotFoundException, SaveException, UnauthorizedException, RefrigeratorNotFoundException {
        GroceryShoppingCart shoppingCartItem = groceryShoppingCartRepository.findById(shoppingCartItemId)
                        .orElseThrow(() -> new NoGroceriesFound("Could not find shopping cart item"));

        long refrigeratorId = shoppingCartItem.getShoppingCart().getShoppingList().getRefrigerator().getId();
        GroceryDTO groceryDTO = new GroceryDTO(shoppingCartItem.getGrocery());
        List<GroceryDTO> groceries = new ArrayList<>();
        groceries.add(groceryDTO);

        SaveGroceryListRequest saveGrocery = new SaveGroceryListRequest(refrigeratorId, groceries, dto.getUnitDTO(), dto.getQuantity());
            groceryService.addGrocery(saveGrocery, httpRequest);
        groceryShoppingCartRepository.delete(shoppingCartItem);
    }

    /**
     * Transfers all groceries from the shopping cart to the refrigerator
     * @param httpRequest http request
     * @throws UserNotFoundException If the user is not found
     * @throws SaveException If there occurred an error while saving
     * @throws UnauthorizedException If not authorized
     * @throws RefrigeratorNotFoundException If no refrigerator was found
     * @throws NoGroceriesFound If no groceries was found in the shopping cart
     */
    @Transactional(propagation =  Propagation.REQUIRED, rollbackFor = Exception.class)
    public void transferAllGroceriesToRefrigerator(SaveGroceryRequest[] request, HttpServletRequest httpRequest) throws UserNotFoundException, SaveException, UnauthorizedException, RefrigeratorNotFoundException, NoGroceriesFound {
        for (SaveGroceryRequest grocery : request) {
            CreateRefrigeratorGroceryDTO dto = CreateRefrigeratorGroceryDTO.builder()
                    .groceryDTO(GroceryDTO.builder()
                            .id(grocery.getGroceryId())
                            .build())
                    .quantity(grocery.getQuantity())
                    .unitDTO(grocery.getUnitDTO())
                    .build();
            transferGroceryToRefrigerator(grocery.getGroceryId(), httpRequest, dto);
        }
    }
}
