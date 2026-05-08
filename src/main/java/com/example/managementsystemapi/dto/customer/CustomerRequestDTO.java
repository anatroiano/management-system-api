package com.example.managementsystemapi.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDTO {

    @Schema(example = "John Doe")
    @Size(max = 255)
    @NotBlank
    private String name;

    @Schema(example = "john.doe@email.com")
    @Email
    private String email;

    @Schema(example = "19999999999")
    private String phone;

    @Schema(example = "12345678900")
    private String document;
}
