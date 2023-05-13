package ntnu.idatt2106.backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.RefrigeratorNotFoundException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.dto.GroceryStatisticDTO;
import ntnu.idatt2106.backend.model.dto.response.ErrorResponse;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.service.GroceryHistoryService;
import ntnu.idatt2106.backend.service.RefrigeratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for stats for groceries eaten or trashed
 */
@RestController
@RequestMapping("/api/history/grocery")
@RequiredArgsConstructor
@Tag(name = "Grocery History Controller", description = "Controller used to retrieve stats for groceries")
public class GroceryHistoryController {
    private final GroceryHistoryService groceryHistoryService;
    private final RefrigeratorService refrigeratorService;

    /**
     * Getter for the stats for groceries eaten or trashed the last year
     * @param refrigeratorId ID to the refrigerator to retrieve history from
     * @param request http request
     * @return list with grocery stats
     * @throws RefrigeratorNotFoundException If the refrigerator was not found
     */
    @Operation(summary = "Get the stats for groceries eaten/trashed the last year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "stats retrieved correctly", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Grocery.class)))),
            @ApiResponse(responseCode = "204", description = "No content - Refrigerator not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/lastYear/{refrigeratorId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getLastYearGroceryStats(@Valid @PathVariable long refrigeratorId, HttpServletRequest request) throws RefrigeratorNotFoundException {
        Refrigerator refrigerator = refrigeratorService.getRefrigerator(refrigeratorId);
        List<GroceryStatisticDTO> stats = groceryHistoryService.getStatsforLastYear(refrigerator.getId());
        return ResponseEntity.ok(stats);
    }

}
