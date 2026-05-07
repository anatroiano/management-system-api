package com.example.managementsystemapi.mapper;

import com.example.managementsystemapi.domain.StockMovement;
import com.example.managementsystemapi.dto.StockMovementResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {

    @Mapping(source = "product.id", target = "productId")
    StockMovementResponseDTO toDTO(StockMovement entity);

}
 