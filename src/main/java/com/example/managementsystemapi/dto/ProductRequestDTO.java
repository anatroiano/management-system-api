package com.example.managementsystemapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotBlank
    @Schema(description = "Product name", example = "Wireless Mouse")
    private String name;

    @Size(max = 1000)
    @Schema(description = "Product description", example = "Ergonomic wireless mouse with USB receiver and long battery life")
    private String description;

    @NotNull
    @Schema(description = "Product price", example = "99.90")
    private BigDecimal price;

}