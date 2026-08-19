package com.example.managementsystemapi.dto.dashboard;

import com.example.managementsystemapi.enums.SaleStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RecentSaleDTO(
        Long id,
        String customerName,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        SaleStatus status
) {
}
