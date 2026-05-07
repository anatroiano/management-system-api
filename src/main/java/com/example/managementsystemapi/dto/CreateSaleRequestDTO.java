package com.example.managementsystemapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSaleRequestDTO {

    @NotNull
    private Long customerId;

    @Valid
    @NotEmpty
    private List<CreateSaleItemRequestDTO> items;

}