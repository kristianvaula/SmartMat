package ntnu.idatt2106.backend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnitTest {

    @Test
     void does_equal_return_true_for_to_equals() {
        Unit unit1 = new Unit(1L, "liter", 1000);
        Unit unit2 = new Unit(1L, "liter", 1000);

        assertEquals(unit1, unit2);
    }

    @Test
    void does_equal_return_false_for_to_unlike_equals() {
        Unit unit1 = new Unit(1L, "liter", 1000);
        Unit unit3 = new Unit(2L, "kg", 2000);

        assertNotEquals(unit1, unit3);
    }

    @Test
    void has_to_equal_objects_the_same_hashCode() {
        Unit unit1 = new Unit(1L, "liter", 1000);
        Unit unit2 = new Unit(1L, "liter", 1000);

        assertEquals(unit1.hashCode(), unit2.hashCode());
    }

    @Test
    void has_to_different_objects_different_hashCode() {
        Unit unit1 = new Unit(1L, "liter", 1000);
        Unit unit3 = new Unit(2L, "kg", 2000);

        assertNotEquals(unit1.hashCode(), unit3.hashCode());
    }

    @Test
    void does_to_string_return_expected_result() {
        Unit unit = new Unit(1L, "liter", 1000);
        String expected = "Unit(id=1, name=liter, weight=1000)";

        String result = unit.toString();

        assertEquals(result, expected);
    }

    @Test
    void is_getter_for_id_returning_correct_id() {
        long id = 1L;
        Unit unit = new Unit(id, "Test", 100);

        long result = unit.getId();

        assertEquals(id, result);
    }

    @Test
    void is_getter_for_name_returning_correct_name() {
        String name = "Test";
        Unit unit = new Unit(1L, name, 100);

        String result = unit.getName();

        assertEquals(name, result);
    }

    @Test
    void is_getter_for_weight_returning_correct_weight() {
        int weight = 100;
        Unit unit = new Unit(1L, "Test", weight);

        int result = unit.getWeight();

        assertEquals(weight, result);
    }

    @Test
    void is_setter_for_id_returning_correct_id() {
        long id = 1L;
        Unit unit = new Unit();

        unit.setId(id);

        assertEquals(id, unit.getId());
    }

    @Test
    void is_setter_for_name_returning_correct_name() {
        String name = "Test";
        Unit unit = new Unit();

        unit.setName(name);

        assertEquals(name, unit.getName());
    }

    @Test
    void is_setter_for_weight_returning_correct_weight() {
        int weight = 100;
        Unit unit = new Unit();

        unit.setWeight(weight);

        assertEquals(weight, unit.getWeight());
    }


    @Test
    void does_unit_builder_return_expected_object() {
        long id = 1L;
        String name = "liter";
        int weight = 1000;

        Unit unit = Unit.builder()
                .id(id)
                .name(name)
                .weight(weight)
                .build();

        assertEquals(id, unit.getId());
        assertEquals(name, unit.getName());
        assertEquals(weight, unit.getWeight());
    }



}
