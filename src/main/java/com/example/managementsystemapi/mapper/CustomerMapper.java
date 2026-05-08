package com.example.managementsystemapi.mapper;

import com.example.managementsystemapi.domain.Customer;
import com.example.managementsystemapi.dto.customer.CustomerRequestDTO;
import com.example.managementsystemapi.dto.customer.CustomerResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toEntity(CustomerRequestDTO dto);

    CustomerResponseDTO toDTO(Customer entity);

    void updateEntity(@MappingTarget Customer entity, CustomerRequestDTO dto);
}