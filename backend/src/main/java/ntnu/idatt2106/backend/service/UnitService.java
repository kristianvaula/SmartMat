package ntnu.idatt2106.backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.Unit;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import ntnu.idatt2106.backend.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The UnitService class provides a method to convert the unit of a grocery
 */
@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;

    /**
     * Provide a grocery and a new unit id to convert the existing unit of the
     * provided grocery into a new unit (based on the provided unit id)
     * @param refrigeratorGrocery the grocery in the refrigerator
     * @param unitId the id of the new unit
     * @return
     */
    public RefrigeratorGrocery convertGrocery(RefrigeratorGrocery refrigeratorGrocery, Long unitId){
        Optional<Unit> newUnit = unitRepository.findById(unitId);
        if(newUnit.isEmpty()){
            throw new EntityNotFoundException("Could not find unit with unitId" + unitId);
        }
        int newQuantity = (refrigeratorGrocery.getQuantity()*refrigeratorGrocery.getUnit().getWeight())/newUnit.get().getWeight();
        refrigeratorGrocery.setQuantity(newQuantity);
        refrigeratorGrocery.setUnit(newUnit.get());
        return refrigeratorGrocery;
    }
}
