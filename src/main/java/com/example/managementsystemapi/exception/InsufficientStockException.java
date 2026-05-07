package com.example.managementsystemapi.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(Long productId, int current, int requested) {
        super(String.format(
                "Insufficient stock for product %s: requested %d, available %d",
                productId, requested, current
        ));
    }
}
 