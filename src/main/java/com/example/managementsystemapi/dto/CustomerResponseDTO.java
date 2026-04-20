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
public class CustomerResponseDTO {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private String document;

    private Boolean active;

    private LocalDateTime createdAt;

}
