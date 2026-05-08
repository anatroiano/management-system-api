package com.example.managementsystemapi.mapper;

import com.example.managementsystemapi.domain.Product;
import com.example.managementsystemapi.dto.product.ProductRequestDTO;
import com.example.managementsystemapi.dto.product.ProductResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequestDTO dto);

    ProductResponseDTO toDTO(Product entity);

    void updateEntity(@MappingTarget Product entity, ProductRequestDTO dto);
}