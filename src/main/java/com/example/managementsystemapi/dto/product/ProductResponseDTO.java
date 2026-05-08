package com.example.managementsystemapi.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    private Long id;

    private String code;

    private String name;

    private String description;

    private BigDecimal price;

    private Boolean active;

    private LocalDateTime createdAt;

}
