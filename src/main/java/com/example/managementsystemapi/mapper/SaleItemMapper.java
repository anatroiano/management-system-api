package com.example.managementsystemapi.mapper;

import com.example.managementsystemapi.domain.SaleItem;
import com.example.managementsystemapi.dto.sale.SaleItemResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleItemMapper {

    @Mapping(source = "product.id", target = "productId")
    SaleItemResponseDTO toDTO(SaleItem entity);

}