package ntnu.idatt2106.backend.service;

import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.GroceryHistory;
import ntnu.idatt2106.backend.model.dto.GroceryStatisticDTO;
import ntnu.idatt2106.backend.model.dto.UnitDTO;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import ntnu.idatt2106.backend.repository.GroceryHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The GroceryHistoryService provides methods to get the stats for the last 12 months
 */
@Service
@RequiredArgsConstructor
public class GroceryHistoryService {

    private final GroceryHistoryRepository groceryHistoryRepository;

    /**
     * Returns a list of all GroceryHistory objects consumed during the specified month x number of months ago for a specific refrigerator.
     * @param x The number of months ago to search for. Must be a non-negative integer.
     * @param refrigeratorId The ID of the refrigerator to search in.
     * @return A List of GroceryHistory objects consumed during the specified month x number of months ago for the specified refrigerator.
     */
    public List<GroceryHistory> findGroceriesForMonth(int x, long refrigeratorId) {
        LocalDate dateXMonthsBack = LocalDate.now().minusMonths(x);
        LocalDate startDate = dateXMonthsBack.withDayOfMonth(1); // Get first day of month x months back
        LocalDate endDate = dateXMonthsBack.withDayOfMonth(dateXMonthsBack.lengthOfMonth()); // Get last day of month x months back
        return groceryHistoryRepository.findByDateConsumedBetweenAndRefrigeratorId(startDate, endDate, refrigeratorId);
    }

    /**
     * Generates statistics for the last 12 months of a specified refrigerator. Deletes old statistics (older than 400 days, or exactly 365 days if change is made) before generating new statistics.
     * @param refrigeratorId the id of the refrigerator for which to generate statistics
     * @return a list of {@code GroceryStatisticDTO} objects representing statistics for each month in the last year
     */
    public List<GroceryStatisticDTO> getStatsforLastYear(long refrigeratorId){
        deleteOldStatistics();
        List<GroceryStatisticDTO> stats = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM, yyyy");
        for(int i = 0; i < 12; i++){
            List<GroceryHistory> iMonth = findGroceriesForMonth(i, refrigeratorId);
            LocalDate dateXMonthsBack = LocalDate.now().minusMonths(i);
            String formattedDate = dateXMonthsBack.format(formatter);

            if(iMonth.size() == 0){
                stats.add(GroceryStatisticDTO.builder()
                        .foodWaste(0)
                        .foodEaten(0)
                        .monthName(formattedDate)
                        .build());
            }
            else{
                stats.add(GroceryStatisticDTO.builder()
                        .foodWaste(sumTrash(iMonth))
                        .foodEaten(sumEaten(iMonth))
                        .monthName(formattedDate)
                        .build());
            }
        }
        return stats;
    }

    /**
     * Calculates the total weight of groceries that were trashed based on the given list of grocery history records.
     * @param list the list of grocery history records to be analyzed
     * @return the total weight of groceries that were trashed, in grams
     */
    public Integer sumTrash(List<GroceryHistory> list) {
        List<GroceryHistory> trash = list.stream()
                .filter(groceryHistory -> groceryHistory.isWasTrashed())
                .collect(Collectors.toList());

        Integer totalWeight = trash.stream()
                .mapToInt(GroceryHistory::getWeightInGrams)
                .sum();

        return totalWeight;
    }

    /**
     * Sums the weight in grams of all the groceries that were consumed (not trashed) in the given list of grocery history.
     * @param list The list of grocery history to be summed.
     * @return The total weight in grams of all the groceries that were consumed in the given list.
     */
    public Integer sumEaten(List<GroceryHistory> list) {
        List<GroceryHistory> eaten = list.stream()
                .filter(groceryHistory -> !groceryHistory.isWasTrashed())
                .collect(Collectors.toList());

        Integer totalWeight = eaten.stream()
                .mapToInt(GroceryHistory::getWeightInGrams)
                .sum();

        return totalWeight;
    }

    /**
     * Deletes old grocery consumption statistics from the database.
     * Statistics older than 400 days (or 365 if you want exactly 12 months) are removed.
     * @throws Exception if there is an error deleting the statistics
     */
    public void deleteOldStatistics() {
        LocalDate dateThreshold = LocalDate.now().minusDays(400); // Change to 365 if you want exactly 12 months
        groceryHistoryRepository.deleteByDateConsumedBefore(dateThreshold);
    }

    /**
     Creates a new grocery history object and saves it to the database.
     @param refrigeratorGrocery the RefrigeratorGrocery object associated with the grocery history
     @param quantity the quantity of the grocery consumed or trashed
     @param unitDTO the UnitDTO object associated with the grocery
     @param isTrash a boolean indicating whether the grocery was trashed or consumed
     */
    public void newGroceryHistory(RefrigeratorGrocery refrigeratorGrocery, int quantity, UnitDTO unitDTO, boolean isTrash){
        GroceryHistory groceryHistory = GroceryHistory.builder()
                .dateConsumed(LocalDate.now())
                .refrigerator(refrigeratorGrocery.getRefrigerator())
                .wasTrashed(isTrash)
                .weightInGrams(quantity * unitDTO.getWeight())
                .build();
        groceryHistoryRepository.save(groceryHistory);
    }

}
