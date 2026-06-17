package com.example.managementsystemapi.dto.stock;

public record StockDashboardDTO(
        long totalActive,
        long totalLowStock,
        long totalOutOfStock
) {
}