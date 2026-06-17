package com.example.managementsystemapi.dto.sale;

import java.math.BigDecimal;

public record SaleDashboardDTO(
        long totalSales,
        BigDecimal totalRevenue,
        BigDecimal averageTicket
) {
}