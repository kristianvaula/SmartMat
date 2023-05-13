package ntnu.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.*;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.dto.MemberDTO;
import ntnu.idatt2106.backend.model.dto.RefrigeratorDTO;
import ntnu.idatt2106.backend.model.dto.UnitDTO;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.model.refrigerator.NewRefrigeratorDTO;
import ntnu.idatt2106.backend.model.requests.MemberRequest;
import ntnu.idatt2106.backend.model.requests.RemoveMemberRequest;
import ntnu.idatt2106.backend.service.CookieService;
import ntnu.idatt2106.backend.service.JwtService;
import ntnu.idatt2106.backend.service.RefrigeratorService;
import ntnu.idatt2106.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for refrigerator
 */
@RestController
    @RequestMapping("/api/refrigerator")
@RequiredArgsConstructor
@Tag(name = "Refrigerator Controller", description = "Controller to handle the refrigerator")
public class RefrigeratorController {

    private final RefrigeratorService refrigeratorService;

    private final CookieService cookieService;

    private final UserService userService;

    private final JwtService jwtService;


    Logger logger = LoggerFactory.getLogger(RefrigeratorController.class);


    /**
     * Edits the favorite refrigerator for a user.
     * @param refrigeratorId the ID of the refrigerator to be set as the favorite
     * @param httpServletRequest the HttpServletRequest object for the current request
     * @return a ResponseEntity with a SuccessResponse object and HTTP status code 200 if the operation was successful
     * @throws UserNotFoundException if the current user cannot be found
     * @throws RefrigeratorNotFoundException if the specified refrigerator cannot be found
     */
    @Operation(summary = "Edit favorite refrigerator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Refrigerator not found")
    })
    @PostMapping("/members/edit-favorite")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse> editFavoriteRefrigerator(@Valid @RequestBody Long refrigeratorId, HttpServletRequest httpServletRequest) throws UserNotFoundException, RefrigeratorNotFoundException {
        logger.info("Received request to set favorite refrigerator id");
        refrigeratorService.setFavoriteRefrigeratorId(refrigeratorId, httpServletRequest);
        return new ResponseEntity<>(new SuccessResponse("Favorite refrigerator set", HttpStatus.OK.value()), HttpStatus.OK);
    }

    /**
     * Edit the role of a member in the refrigerator
     * @param memberRequest - The request body containing the member details to be updated
     * @param httpRequest - The HTTP request object
     * @return - ResponseEntity<MemberDTO> with updated member details if successful, otherwise an HTTP status code
     * @throws Exception - Throws an exception if there was an error while updating the member details
     */
    @Operation(summary = "Edit role of a refrigerator member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role edited successfully", content = @Content(schema = @Schema(implementation = MemberDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/members/edit-role")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MemberDTO> editRole(@Valid @RequestBody MemberRequest memberRequest, HttpServletRequest httpRequest) {
        logger.info("Received request to edit member role in refrigerator");
        MemberDTO result;
        try {
            result = refrigeratorService.setFridgeRole(memberRequest, httpRequest);
            if (result == null) throw new Exception();
        } catch(LastSuperuserException e){
          logger.warn("Could not edit role: Is last superuser in refrigerator");
          return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Could not edit role: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("Returning response");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Edit the roles of multiple members in the refrigerator
     * @param memberRequests - The request body containing an array of member details to be updated
     * @param httpRequest - The HTTP request object
     * @return - ResponseEntity<MemberDTO[]> with updated member details if successful, otherwise an HTTP status code
     * @throws Exception - Throws an exception if there was an error while updating the member details
     */
    @Operation(summary = "Edit role of a number of refrigerator members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles edited successfully", content = @Content(schema = @Schema(implementation = MemberDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/members/edit-roles")
    @PreAuthorize("isAuthenticated()")
    @Transactional(propagation =  Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResponseEntity<MemberDTO[]> editRoles(@Valid @RequestBody MemberRequest[] memberRequests, HttpServletRequest httpRequest) throws Exception {
        logger.info("Received request to edit member role in refrigerator");
        MemberDTO[] result = new MemberDTO[memberRequests.length];
        try {
            for (int i = result.length-1; i >= 0; i--) {
                result[i] = refrigeratorService.setFridgeRole(memberRequests[i], httpRequest);
            }
        } catch (Exception e) {
            logger.error("Could not edit role: " + e.getMessage());
            throw e;
        }
        logger.info("Returning response");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Add a new member to the refrigerator
     * @param memberRequest - The request body containing the details of the new member
     * @param httpRequest - The HTTP request object
     * @return - ResponseEntity<MemberDTO> with the details of the new member if successful, otherwise an HTTP status code
     * @throws SaveException - Throws a SaveException if there was an error while saving the new member details
     */
    @Operation(summary = "Add a new member to a refrigerator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member added successfully", content = @Content(schema = @Schema(implementation = MemberDTO.class))),
            @ApiResponse(responseCode = "500", description = "Failed to add member")
    })
    @PostMapping("/members/new")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MemberDTO> newMember(@Valid @RequestBody MemberRequest memberRequest, HttpServletRequest httpRequest) throws SaveException {
        logger.info("Received request to add new member to refrigerator");
        MemberDTO result;
        try {
            result = refrigeratorService.addMember(memberRequest, httpRequest);
            if (result == null) throw new Exception();
        } catch (Exception e) {
            throw new SaveException("Failed to add member");
        }
        logger.info("Returning response");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(summary = "Remove a member from a refrigerator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member removed successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/members/remove")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse> removeMember(@Valid @RequestBody RemoveMemberRequest memberRequest, HttpServletRequest httpRequest) throws Exception {
        logger.info("Received request to remove member from refrigerator");
        refrigeratorService.removeUserFromRefrigerator(memberRequest, httpRequest);
        logger.info("Member removed successfully");
        return new ResponseEntity<>(new SuccessResponse("Member removed successfully", HttpStatus.OK.value()), HttpStatus.OK);
    }

    /**
     * Creates a new refrigerator
     * @param refrigerator The details of the new refrigerator to be created
     * @param httpRequest The HttpServletRequest object that contains the HTTP request sent by the client
     * @return The newly created refrigerator
     * @throws SaveException If the refrigerator cannot be created due to a server-side error
     */
    @Operation(summary = "Create a new refrigerator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refrigerator created successfully", content = @Content(schema = @Schema(implementation = Refrigerator.class))),
            @ApiResponse(responseCode = "500", description = "Failed to create refrigerator")
    })
    @PostMapping("/new")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Refrigerator> newRefrigerator(@Valid @RequestBody NewRefrigeratorDTO refrigerator, HttpServletRequest httpRequest) throws SaveException {
        logger.info("Received request to create refrigerator for refrigerator");
        Refrigerator result;

        try {
            result = refrigeratorService.save(refrigerator, httpRequest);
            if (result == null) throw new Exception();
        } catch (Exception e) {
            logger.error("Failed to create refrigerator: " + e.getMessage());
            throw new SaveException("Failed to create refrigerator");
        }
        logger.info("Returning refrigerator with id {}", result.getId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Edits an existing refrigerator
     * @param refrigerator The details of the refrigerator to be edited
     * @param httpRequest The HttpServletRequest object that contains the HTTP request sent by the client
     * @return A success response indicating that the refrigerator was edited successfully
     * @throws UserNotFoundException If the user cannot be found
     * @throws SaveException If the refrigerator cannot be edited due to a server-side error
     * @throws UnauthorizedException If the user is not authorized to edit the refrigerator
     * @throws RefrigeratorNotFoundException If the refrigerator cannot be found
     */
    @Operation(summary = "Edits a refrigerator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refrigerator updated successfully", content = @Content(schema = @Schema(implementation = Refrigerator.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Failed to create refrigerator"),
    })
    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse> editRefrigerator(@Valid @RequestBody RefrigeratorDTO refrigerator, HttpServletRequest httpRequest) throws UserNotFoundException, SaveException, UnauthorizedException, RefrigeratorNotFoundException {
        logger.info("Received request to edit refrigerator");
        refrigeratorService.editRefrigerator(refrigerator, httpRequest);
        return new ResponseEntity<>(new SuccessResponse("Refrigerator updated successfully", HttpStatus.OK.value()), HttpStatus.OK);
    }

    /**
     * Deletes a refrigerator by its ID and username
     * @param refrigeratorId The ID of the refrigerator to be deleted
     * @param httpRequest The HttpServletRequest object that contains the HTTP request sent by the client
     * @return A success response indicating that the refrigerator was deleted successfully
     * @throws Exception If the refrigerator cannot be deleted due to a server-side error
     */
    @Operation(summary = "Delete a refrigerator by ID and username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refrigerator deleted successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete/{refrigeratorId}/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse> deleteRefrigerator(@Valid @PathVariable int refrigeratorId, HttpServletRequest httpRequest) throws Exception {
        refrigeratorService.forceDeleteRefrigerator(refrigeratorId, httpRequest);
        logger.info("Member removed successfully");
        return new ResponseEntity<>(new SuccessResponse("Member removed successfully", HttpStatus.OK.value()), HttpStatus.OK);
    }

    /**
     * This method gets all refrigerators associated with the authenticated user
     * @param request the HTTP servlet request that contains the authenticated user's information
     * @return a response entity containing a list of refrigerators and the HTTP status code
     * @throws UserNotFoundException if the authenticated user is not found in the system
     */
    @Operation(summary = "Get all refrigerators by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of refrigerators fetched successfully", content = @Content(schema = @Schema(implementation = Refrigerator.class))),
            @ApiResponse(responseCode = "204", description = "User not found")
    })
    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Refrigerator>> getAllByUser(HttpServletRequest request) throws UserNotFoundException {
        logger.info("Received request for all refrigerators by user");
        List<Refrigerator> result = refrigeratorService.getAllByUser(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * This method gets a specific refrigerator by its id
     * @param refrigeratorId the id of the refrigerator to retrieve
     * @param httpServletRequest the HTTP servlet request that contains the authenticated user's information
     * @return a response entity containing a refrigerator DTO and the HTTP status code
     * @throws UserNotFoundException if the authenticated user is not found in the system
     * @throws RefrigeratorNotFoundException if the refrigerator with the given id is not found in the system
     */
    @Operation(summary = "Get refrigerators by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of refrigerators fetched successfully", content = @Content(schema = @Schema(implementation = Refrigerator.class))),
            @ApiResponse(responseCode = "204", description = "No content")
    })
    @GetMapping("/{refrigeratorId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RefrigeratorDTO> getById(@Valid @PathVariable long refrigeratorId, HttpServletRequest httpServletRequest) throws UserNotFoundException, RefrigeratorNotFoundException {
        logger.info("Received request for refrigerator with id: {}", refrigeratorId);

        RefrigeratorDTO result = refrigeratorService.getRefrigeratorDTOById(refrigeratorId, httpServletRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * This method gets all available units
     * @return a response entity containing a list of available units and the HTTP status code
     */
    @GetMapping("/units")
    public ResponseEntity<?> getUnits(){
        logger.info("Received request for retrieving all units");

        List<UnitDTO> list = refrigeratorService.getUnits();
        return ResponseEntity.ok(list);
    }

    /**
     * Handle exceptions from @Valid annotation
     * @param ex exception to handle
     * @return errors
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
