package com.example.managementsystemapi.dto;

import com.example.managementsystemapi.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementResponseDTO {

    private Long id;

    private Long productId;

    private MovementType type;

    private Integer quantity;

    private String reason;

    private LocalDateTime createdAt;


}