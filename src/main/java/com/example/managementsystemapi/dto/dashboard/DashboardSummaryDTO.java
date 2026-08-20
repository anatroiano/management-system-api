package com.example.managementsystemapi.dto.dashboard;

import java.math.BigDecimal;

public record DashboardSummaryDTO(
        Long totalCustomers,
        Long totalProducts,
        Long salesToday,
        BigDecimal currentMonthRevenue
) {
}
