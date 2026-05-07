package com.example.managementsystemapi.domain;

import com.example.managementsystemapi.enums.MovementType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

@Entity
@Table(name = "stock_movements", indexes = {
        @Index(name = "idx_stock_movement_product_id", columnList = "product_id"),
        @Index(name = "idx_stock_movement_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockMovement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType type;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 255)
    private String reason;

    public static StockMovement of(
            Product product,
            MovementType type,
            int quantity,
            String reason
    ) {
        Assert.isTrue(quantity > 0, "Quantity must be positive");
        Assert.notNull(product.getId(), "Product ID must not be null");
        Assert.notNull(type, "Movement type must not be null");

        var movement = new StockMovement();
        movement.product = product;
        movement.type = type;
        movement.quantity = quantity;
        movement.reason = reason;
        return movement;
    }
}