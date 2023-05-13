package ntnu.idatt2106.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


/**
 * Model class representing a shopping list.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ShoppingList")
@Schema(description = "A registered refrigerator of the application")
@Entity
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id to the refrigerator, automatically generated")
    private long id;

    @OneToOne()
    @JoinColumn(name = "refrigeratorId")
    @Schema(description = "The refrigerator connected to the shopping list")
    private Refrigerator refrigerator;
}

