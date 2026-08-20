package com.example.managementsystemapi.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalesByDayDTO(
        LocalDate date,
        Long salesCount,
        BigDecimal totalAmount
) {
}
