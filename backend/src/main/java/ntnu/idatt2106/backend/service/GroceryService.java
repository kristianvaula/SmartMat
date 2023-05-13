package ntnu.idatt2106.backend.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.*;
import ntnu.idatt2106.backend.exceptions.NoSuchElementException;
import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.model.dto.DeleteRefrigeratorGroceryDTO;
import ntnu.idatt2106.backend.model.dto.GroceryDTO;
import ntnu.idatt2106.backend.model.dto.RefrigeratorGroceryDTO;
import ntnu.idatt2106.backend.model.dto.shoppingListElement.GroceryDTOComparator;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import ntnu.idatt2106.backend.model.recipe.RecipeGrocery;
import ntnu.idatt2106.backend.model.requests.SaveGroceryListRequest;
import ntnu.idatt2106.backend.repository.GroceryRepository;
import ntnu.idatt2106.backend.repository.RefrigeratorGroceryRepository;
import ntnu.idatt2106.backend.repository.SubCategoryRepository;
import ntnu.idatt2106.backend.repository.UnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for handling operations regarding groceries inside a refrigerator
 */
@Service
@RequiredArgsConstructor
public class GroceryService {

    private static final FridgeRole ADD_PRIVILEGE = FridgeRole.SUPERUSER;
    private static final FridgeRole REMOVE_PRIVILEGE = FridgeRole.SUPERUSER;

    private final Logger logger = LoggerFactory.getLogger(GroceryService.class);

    private final CookieService cookieService;
    private final JwtService jwtService;
    private final RefrigeratorGroceryRepository refrigeratorGroceryRepository;
    private final GroceryRepository groceryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final RefrigeratorService refrigeratorService;
    private final NotificationService notificationService;
    private final UnitService unitService;
    private final GroceryHistoryService groceryHistoryService;

    private final UnitRepository unitRepository;

    /**
     * Saves a grocery to a refrigerator. If it is a custom
     * grocery, it gets added to the groceries first.
     *
     * @param saveRequest Request to save a number of groceries
     * @param httpRequest The http request
     * @throws RefrigeratorNotFoundException If refrigerator not found
     * @throws UserNotFoundException If user not found
     * @throws UnauthorizedException If user not authorized to perform action
     * @throws SaveException If save fails
     */
    @Transactional(propagation =  Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addGrocery(SaveGroceryListRequest saveRequest, HttpServletRequest httpRequest) throws RefrigeratorNotFoundException, UserNotFoundException, UnauthorizedException, SaveException {
        Refrigerator refrigerator = refrigeratorService.getRefrigerator(saveRequest.getRefrigeratorId());
        FridgeRole FridgeRole = getFridgeRole(refrigerator, httpRequest);
        if(FridgeRole != ADD_PRIVILEGE) throw new UnauthorizedException("User not authorized to add groceries");

        logger.info("Saving grocery list to refrigerator");
        //Handle each grocery in the list individually based on custom grocery or existing
        for (GroceryDTO groceryDTO: saveRequest.getGroceryList()) {
            Grocery grocery;
            //If custom add custom grocery, else fetch
            if(groceryDTO.isCustom()) {
                grocery = addCustomGrocery(groceryDTO);
            } else {
                logger.debug("Fetching grocery with ID: " + groceryDTO.getId());
                grocery = getGroceryById(groceryDTO.getId());
                logger.debug("Fetched grocery: " + grocery);
            }

            //Define refrigerator grocery
            RefrigeratorGrocery refrigeratorGrocery = new RefrigeratorGrocery();
            refrigeratorGrocery.setGrocery(grocery);
            refrigeratorGrocery.setRefrigerator(refrigerator);
            refrigeratorGrocery.setPhysicalExpireDate(getPhysicalExpireDate(groceryDTO.getGroceryExpiryDays()));
            Optional<Unit> unit = unitRepository.findById(saveRequest.getUnitDTO().getId());
            refrigeratorGrocery.setUnit(unit.get());
            refrigeratorGrocery.setQuantity(saveRequest.getQuantity());
            saveRefrigeratorGrocery(refrigeratorGrocery);
        }
    }

    /**
     * Adds a custom grocery to the grocery list
     *
     * @param groceryDTO the data received from request
     * @return the added grocery
     * @throws SaveException if save fails
     */
    public Grocery addCustomGrocery(GroceryDTO groceryDTO) throws SaveException {
        Grocery newGrocery = new Grocery();
        newGrocery.setName(groceryDTO.getName());
        SubCategory subCategory = subCategoryRepository.findById(groceryDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found by id"));
        newGrocery.setSubCategory(subCategory);
        if(groceryDTO.getDescription() != null){
            newGrocery.setDescription(groceryDTO.getDescription());
        }
        newGrocery.setGroceryExpiryDays(groceryDTO.getGroceryExpiryDays());

        return saveGrocery(newGrocery);
    }

    /**
     * Extracts username from cookie
     * @param httpRequest Request we are extracting from
     * @return username
     */
    protected String extractEmail(HttpServletRequest httpRequest) {
        return jwtService.extractUsername(cookieService.extractTokenFromCookie(httpRequest));
    }

    /**
     * Gets a list of DTO's representing groceries
     * in refrigerator.
     *
     * @param refrigeratorId Refrigerator id
     * @param request Http request
     * @return List of RefrigeratorGrocery DTO's
     * @throws RefrigeratorNotFoundException If refrigerator not found
     * @throws UserNotFoundException if user not found
     * @throws UnauthorizedException if user not member
     */
    public List<RefrigeratorGroceryDTO> getGroceriesByRefrigerator(long refrigeratorId, HttpServletRequest request) throws RefrigeratorNotFoundException, UserNotFoundException, UnauthorizedException {
        //Throws if refrigerator does not exist
        Refrigerator refrigerator = refrigeratorService.getRefrigerator(refrigeratorId);
        getFridgeRole(refrigerator,request);

        List<RefrigeratorGrocery> groceries = refrigeratorGroceryRepository.findAllByRefrigeratorId(refrigerator.getId());
        List<RefrigeratorGroceryDTO> refGroceryDTOS = new ArrayList<>();
        for (RefrigeratorGrocery grocery: groceries) {
            refGroceryDTOS.add(new RefrigeratorGroceryDTO(grocery));
        }
        return refGroceryDTOS;
    }

    /**
     * Gets grocery by id
     *
     * @param groceryId id of grocery
     * @return the grocery
     */
    public Grocery getGroceryById(long groceryId){
        return groceryRepository.findById(groceryId)
                .orElseThrow(() -> new EntityNotFoundException("Existing grocery not found"));
    }

    /**
     * Gets refigeratorGrocery by id
     *
     * @param refrigeratorGroceryId refigeratorGrocery id
     * @return the refrigeratorGrocery
     */
    public RefrigeratorGrocery getRefrigeratorGroceryById(long refrigeratorGroceryId) throws EntityNotFoundException{
        return refrigeratorGroceryRepository.findById(refrigeratorGroceryId)
                .orElseThrow(() -> new EntityNotFoundException("refrigeratorGrocery not found"));
    }

    /**
     * Gets the FridgeRole of user that requested action
     *
     * @param refrigerator refrigerator FridgeRole is in
     * @param request request to api
     * @return FridgeRole of user
     * @throws UserNotFoundException if user not found
     * @throws UnauthorizedException if user not member
     */
    public FridgeRole getFridgeRole(Refrigerator refrigerator, HttpServletRequest request) throws UserNotFoundException, UnauthorizedException {
        logger.info("Checking if user is member");
        String email = extractEmail(request);
        //Throws if user is not member
        return refrigeratorService.getFridgeRole(refrigerator, email);
    }

    /**
     * Creates a physical expire date by getting todays
     * date and adding integer number of days
     *
     * @param groceryExpiryDays expected shelf life
     * @return expected expiry date
     */
    public LocalDate getPhysicalExpireDate(int groceryExpiryDays) {
        return LocalDate.now().plus(groceryExpiryDays, ChronoUnit.DAYS);
    }

    /**
     * Method for fetching groceries from refrigerator
     * that matches a recipes groceris
     *
     * @param recipeGroceries RecipeGroceries in recipe
     * @param refrigeratorId Refrigerator ID
     * @return Map binding grocery id to refrigeratorGroceryDTO
     */
    public HashMap<Long,List<RefrigeratorGroceryDTO>> getIngredientsInRefrigerator(List<RecipeGrocery> recipeGroceries, long refrigeratorId) {
        HashMap<Long,List<RefrigeratorGroceryDTO>> result = new HashMap<Long, List<RefrigeratorGroceryDTO>>();
        List<RefrigeratorGrocery> refrigeratorGroceries = refrigeratorGroceryRepository.findAllByRefrigeratorId(refrigeratorId);
        for (RecipeGrocery recipeGrocery : recipeGroceries) {
            long groceryId = recipeGrocery.getGrocery().getId();
            for(RefrigeratorGrocery refrigeratorGrocery : refrigeratorGroceries){
                if(refrigeratorGrocery.getGrocery().getId() == groceryId){
                    if(!result.containsKey(groceryId)){
                        result.put(groceryId, new ArrayList<>());
                    }
                    result.get(groceryId).add(new RefrigeratorGroceryDTO(refrigeratorGrocery));
                }
            }
        }
        return result;
    }

    /**
     * Removes a refrigeratorGrocery by id
     *
     * @param refrigeratorGroceryId id
     * @param request http request by user
     * @throws UserNotFoundException If refrigeratorGrocery not found
     * @throws UnauthorizedException If user does not have permission
     */
    @Transactional(propagation =  Propagation.REQUIRED, rollbackFor = Exception.class)
    public void removeRefrigeratorGrocery(long refrigeratorGroceryId, HttpServletRequest request) throws UserNotFoundException, UnauthorizedException, EntityNotFoundException {
        RefrigeratorGrocery refrigeratorGrocery = getRefrigeratorGroceryById(refrigeratorGroceryId);
        FridgeRole userRole = getFridgeRole(refrigeratorGrocery.getRefrigerator(), request);
        if(userRole == null){
            throw new UnauthorizedException("User is not a member of the refrigerator");
        }
        refrigeratorGroceryRepository.deleteById(refrigeratorGroceryId);
    }

    /**
     * Saves a grocery to the grocery table
     *
     * @param grocery the grocery to be saved
     * @throws SaveException If save fails
     */
    public Grocery saveGrocery(Grocery grocery) throws SaveException{
        try {
            return groceryRepository.save(grocery);
        } catch (Exception e) {
            throw new SaveException(e.getMessage());
        }
    }

    /**
     * Saves a refrigerator grocery to the refrigeratorGrocery table
     *
     * @param grocery the grocery to be saved
     * @throws SaveException If save fails
     */
    public void saveRefrigeratorGrocery(RefrigeratorGrocery grocery) throws SaveException {
        try {
            refrigeratorGroceryRepository.save(grocery);
        } catch (Exception e) {
            throw new SaveException(e.getMessage());
        }
    }


    /**
     Returns a list of all GroceryDTO objects.
     @return a List of GroceryDTO objects
     @throws NoGroceriesFound if no groceries are found in the database
     */
    public List<GroceryDTO> getAllGroceriesDTO() throws NoGroceriesFound {
        List<Grocery> groceries = groceryRepository.findAll();
        if (groceries.isEmpty()) {
            logger.info("Could not find any groceries");
            throw new NoGroceriesFound("Could not find any groceries");
        }
        return groceries.stream().map(GroceryDTO::new).sorted(new GroceryDTOComparator()).collect(Collectors.toList());
    }

    /**
     * This method is used to eat a RefrigeratorGrocery item from a refrigerator. It first retrieves the grocery item by its id,
     * then uses the 'useRefrigeratorGrocery' method to update the quantity of the item. If the item does not exist, a
     * 'NoSuchElementException' will be thrown. After the quantity is updated, a new grocery history is created using the
     * 'groceryHistoryService' to track the item's usage.
     * @param dto the DeleteRefrigeratorGroceryDTO object containing the details of the grocery item to eat
     * @param request the HttpServletRequest object to get the current user's information
     * @return the updated RefrigeratorGrocery object
     * @throws Exception if the grocery item does not exist or if the user is not authorized to perform the operation
     * @throws NoSuchElementException if the grocery item does not exist
     */
    public RefrigeratorGrocery eatRefrigeratorGrocery(DeleteRefrigeratorGroceryDTO dto, HttpServletRequest request) throws Exception {
        Optional<RefrigeratorGrocery> grocery = refrigeratorGroceryRepository.findById(dto.getRefrigeratorGroceryDTO().getId());
        RefrigeratorGrocery result = useRefrigeratorGrocery(dto, request);
        if(grocery.isEmpty()){
            throw new NoSuchElementException("Could not find grocery with id: " + dto.getRefrigeratorGroceryDTO().getId());
        }
        logger.info("Creating history object");
        groceryHistoryService.newGroceryHistory(grocery.get(), dto.getQuantity(), dto.getUnitDTO(), false);
        return result;
    }

    /**
     * Trashes the given {@link RefrigeratorGrocery} object based on the information provided in the given {@link DeleteRefrigeratorGroceryDTO} object.
     * @param dto the {@link DeleteRefrigeratorGroceryDTO} object containing the information needed to trash the grocery
     * @param request the {@link HttpServletRequest} object containing the request information
     * @return the trashed {@link RefrigeratorGrocery} object
     * @throws Exception if the grocery cannot be found, the user does not have permission to perform the action, or if there is an issue with the notification service
     */
    public RefrigeratorGrocery trashRefrigeratorGrocery(DeleteRefrigeratorGroceryDTO dto, HttpServletRequest request) throws Exception {
        Optional<RefrigeratorGrocery> grocery = refrigeratorGroceryRepository.findById(dto.getRefrigeratorGroceryDTO().getId());
        RefrigeratorGrocery result = useRefrigeratorGrocery(dto, request);
        if(grocery.isEmpty()){
            throw new NoSuchElementException("Could not find grocery with id: " + dto.getRefrigeratorGroceryDTO().getId());
        }
        logger.info("Creating history object");
        groceryHistoryService.newGroceryHistory(grocery.get(),dto.getQuantity(), dto.getUnitDTO(), true);
        return result;
    }

    /**
     * Uses a {@link RefrigeratorGrocery} object by updating its quantity and unit based on the given {@link DeleteRefrigeratorGroceryDTO}.
     * @param dto the {@link DeleteRefrigeratorGroceryDTO} object containing the information needed to update the grocery
     * @param request the {@link HttpServletRequest} object representing the incoming request
     * @return the updated {@link RefrigeratorGrocery} object, or null if the quantity was not updated
     * @throws NoSuchElementException if the grocery with the given ID cannot be found
     * @throws UnauthorizedException if the user is not authorized to perform the action
     * @throws Exception if there is an error converting the grocery unit or deleting notifications
     */
    public RefrigeratorGrocery useRefrigeratorGrocery(DeleteRefrigeratorGroceryDTO dto, HttpServletRequest request) throws Exception {
        Optional<RefrigeratorGrocery> grocery = refrigeratorGroceryRepository.findById(dto.getRefrigeratorGroceryDTO().getId());
        if(grocery.isEmpty()){
            throw new NoSuchElementException("Could not find grocery with id: " + dto.getRefrigeratorGroceryDTO().getId());
        }
        FridgeRole userRole = getFridgeRole(grocery.get().getRefrigerator(), request);
        if(userRole == null){
            throw new UnauthorizedException("User is not a member of the refrigerator");
        }
        RefrigeratorGrocery newGrocery = unitService.convertGrocery(grocery.get(), dto.getUnitDTO().getId());
        if(newGrocery.getQuantity() - dto.getQuantity() <= 0){
            notificationService.deleteNotificationsByRefrigeratorGrocery(grocery.get());
            removeRefrigeratorGrocery(grocery.get().getId(), request);
            return newGrocery;
        }
        else{
            if(newGrocery.getUnit().getId() == dto.getUnitDTO().getId()){
                newGrocery.setQuantity(newGrocery.getQuantity() - dto.getQuantity());
                refrigeratorGroceryRepository.save(newGrocery);
            }
        }
        return null;
    }

    /**
     * Updates a {@link RefrigeratorGrocery} object with the given information.
     * @param refrigeratorGroceryDTO the {@link RefrigeratorGroceryDTO} object containing the updated information
     * @param request the {@link HttpServletRequest} object representing the incoming request
     * @throws UserNotFoundException if the user associated with the request cannot be found
     * @throws UnauthorizedException if the user does not have permission to update the grocery
     * @throws NotificationException if there is an error deleting notifications associated with the grocery
     * @throws NoSuchElementException if the grocery with the given ID cannot be found
     */
    public void updateRefrigeratorGrocery(RefrigeratorGroceryDTO refrigeratorGroceryDTO, HttpServletRequest request) throws UserNotFoundException, UnauthorizedException, NotificationException, NoSuchElementException {
        Optional<RefrigeratorGrocery> oldGrocery = refrigeratorGroceryRepository.findById(refrigeratorGroceryDTO.getId());
        if(oldGrocery.isEmpty()){
            throw new NoSuchElementException("Could not find grocery with id: " + refrigeratorGroceryDTO.getId());
        }
        FridgeRole userRole = getFridgeRole(oldGrocery.get().getRefrigerator(), request);
        if(userRole.equals(FridgeRole.USER)){
            throw new UnauthorizedException("User does not have permission to do updates in this refrigerator");
        }

        if(!refrigeratorGroceryDTO.getPhysicalExpireDate().equals(oldGrocery.get().getPhysicalExpireDate())){
            notificationService.deleteNotificationsByRefrigeratorGrocery(oldGrocery.get());
        }
        RefrigeratorGrocery newGrocery = RefrigeratorGrocery.builder()
                .physicalExpireDate(refrigeratorGroceryDTO.getPhysicalExpireDate())
                .grocery(oldGrocery.get().getGrocery())
                .refrigerator(oldGrocery.get().getRefrigerator())
                .id(refrigeratorGroceryDTO.getId())
                .quantity(refrigeratorGroceryDTO.getQuantity())
                .unit(oldGrocery.get().getUnit())
        .build();
        refrigeratorGroceryRepository.save(newGrocery);
    }

    /**
     * Creates a new {@link Grocery} object based on the information provided in the given {@link GroceryDTO} object.
     * @param grocery the {@link GroceryDTO} object containing the information needed to create the new grocery
     * @return the created {@link Grocery} object
     */
    public Grocery createGrocery(GroceryDTO grocery) {
        //convert from dto to entity
        Grocery newGrocery = Grocery.builder()
                .id(grocery.getId())
                .name(grocery.getName())
                .groceryExpiryDays(grocery.getGroceryExpiryDays())
                .build();
        return groceryRepository.save(newGrocery);
    }


}
