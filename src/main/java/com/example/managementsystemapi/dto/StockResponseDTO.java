package com.example.managementsystemapi.dto;

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

    private Integer quantity;

    private LocalDateTime updatedAt;


}