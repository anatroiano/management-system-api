package com.example.managementsystemapi.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockResponseDTO {

    private Long id;

    private Long productId;

    private String productCode;

    private String productName;

    private Integer quantity;

    private LocalDateTime updatedAt;


}