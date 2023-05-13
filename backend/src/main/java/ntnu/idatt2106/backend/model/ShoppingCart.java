package ntnu.idatt2106.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


/**
 * Model representing a shopping cart, this holds groceries.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ShoppingCart")
@Schema(description = "Shopping cart for one shopping list of the application")
@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of the shopping cart, automatically generated")
    private long id;

    @OneToOne
    @JoinColumn(name = "shoppingListId")
    @Schema(description = "The shopping list connected to the shopping cart")
    private ShoppingList shoppingList;
}
