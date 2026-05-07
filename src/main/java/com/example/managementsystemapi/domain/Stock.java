package com.example.managementsystemapi.domain;

import com.example.managementsystemapi.exception.InsufficientStockException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Version
    private Long version;

    public static Stock createFor(Product product) {
        var stock = new Stock();

        stock.product = product;
        stock.quantity = 0;

        return stock;
    }

    public void addEntry(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        this.quantity += quantity;
    }

    public void addExit(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        if (this.quantity < quantity) {
            throw new InsufficientStockException(product.getId(), this.quantity, quantity);
        }
        this.quantity -= quantity;
    }

}