package com.example.managementsystemapi.dto;

import com.example.managementsystemapi.enums.SaleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponseDTO {

    private Long id;

    private Long customerId;

    private SaleStatus status;

    private BigDecimal totalAmount;

    private List<SaleItemResponseDTO> items;

    private LocalDateTime createdAt;

}