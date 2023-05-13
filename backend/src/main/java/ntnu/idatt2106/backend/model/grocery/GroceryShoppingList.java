package ntnu.idatt2106.backend.model.grocery;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.ShoppingList;
import ntnu.idatt2106.backend.model.Unit;

/**
 * Connection between a Grocery and a Shopping list. Can be requested if its from a normal User. Represents a shopping list entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "GroceryShoppingList")
@Schema(description = "Connection table between groceries and the shopping list")
@Entity
public class GroceryShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id for the connection between shopping list and grocery, automatically generated")
    private long id;

    @Column(name = "isRequested")
    @Schema(description = "Boolean value as represents if a limited user or a super user has added the grocery to the list.")
    private boolean isRequest;

    @Column(name = "quantity")
    @Schema(description = "The number of groceries to buy")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "unitId")
    @Schema(description = "The unit of a grocery")
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "groceryId")
    @Schema(description = "Grocery object in the list")
    private Grocery grocery;

    @ManyToOne
    @JoinColumn(name = "shoppingListId")
    @Schema(description = "The shopping list connected to the grocery")
    private ShoppingList shoppingList;

    /**
     * Adds the amount in the param to the quantity if it is greater than 0
     * @return New quantity
     */
    public int editQuantity(int amount, Unit unit) {
        if(this.unit.getWeight() > unit.getWeight()){
            this.quantity = (this.unit.getWeight()*this.quantity + unit.getWeight()*amount)/unit.getWeight();
            this.unit = unit;
            return this.quantity;
        }
        else{
            this.quantity = (this.unit.getWeight() * this.quantity + unit.getWeight()*amount)/this.unit.getWeight();
            return (this.unit.getWeight() * this.quantity + unit.getWeight()*amount)/this.unit.getWeight();
        }
    }
}
