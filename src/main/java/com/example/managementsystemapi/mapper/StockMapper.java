package com.example.managementsystemapi.mapper;

import com.example.managementsystemapi.domain.Stock;
import com.example.managementsystemapi.dto.stock.StockResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMapper {

    @Mapping(source = "product.id", target = "productId")
    StockResponseDTO toDTO(Stock entity);

}
 