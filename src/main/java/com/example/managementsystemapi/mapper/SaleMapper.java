package com.example.managementsystemapi.mapper;

import com.example.managementsystemapi.domain.Sale;
import com.example.managementsystemapi.dto.sale.SaleResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = SaleItemMapper.class)
public interface SaleMapper {

    @Mapping(source = "customer.id", target = "customerId")
    SaleResponseDTO toDTO(Sale entity);

}