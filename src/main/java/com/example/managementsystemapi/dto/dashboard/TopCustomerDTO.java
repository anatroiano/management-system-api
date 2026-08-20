package com.example.managementsystemapi.dto.dashboard;

import java.math.BigDecimal;

public record TopCustomerDTO(
        Long id,
        String customerName,
        Long purchaseCount,
        BigDecimal totalSpent
) {
}
