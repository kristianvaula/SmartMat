package ntnu.idatt2106.backend.service;

import jakarta.persistence.EntityNotFoundException;
import ntnu.idatt2106.backend.model.Unit;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import ntnu.idatt2106.backend.repository.UnitRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class UnitServiceTest {

    @Mock
    private UnitRepository unitRepository;

    private UnitService unitService;

    public UnitServiceTest() {
        MockitoAnnotations.openMocks(this);
        unitService = new UnitService(unitRepository);
    }

    @Test
    void convertGrocery_ShouldThrowException_WhenUnitIdNotFound() {
        // Arrange
        RefrigeratorGrocery grocery = new RefrigeratorGrocery();
        grocery.setQuantity(100);
        Unit unit = new Unit();
        unit.setWeight(500);
        grocery.setUnit(unit);
        long nonExistingUnitId = 123L;
        when(unitRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            unitService.convertGrocery(grocery, nonExistingUnitId);
        });
    }

    @Test
    void convertGrocery_ShouldReturnSameGrocery_WhenUnitIdExists() {

        RefrigeratorGrocery grocery = new RefrigeratorGrocery();
        grocery.setQuantity(100);
        Unit originalUnit = new Unit();
        originalUnit.setId(1L); // set the id to match the new unit's id
        originalUnit.setWeight(500);
        grocery.setUnit(originalUnit);
        Long existingUnitId = 1L;
        Unit newUnit = new Unit();
        newUnit.setId(1L);
        newUnit.setWeight(250);
        when(unitRepository.findById(1L)).thenReturn(Optional.ofNullable(newUnit));


        RefrigeratorGrocery convertedGrocery = unitService.convertGrocery(grocery, existingUnitId);


        Assertions.assertEquals(200, convertedGrocery.getQuantity());
        Assertions.assertEquals(newUnit.getId(), convertedGrocery.getUnit().getId()); // compare only the id values
    }
}
