package com.example.managementsystemapi.dto.dashboard;

import java.math.BigDecimal;

public record TopProductDTO(
        String productName,
        Long totalQuantitySold,
        BigDecimal totalRevenue
) {
}
