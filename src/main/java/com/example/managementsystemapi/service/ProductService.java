package com.example.managementsystemapi.service;

import com.example.managementsystemapi.domain.Product;
import com.example.managementsystemapi.dto.product.ProductRequestDTO;
import com.example.managementsystemapi.dto.product.ProductResponseDTO;
import com.example.managementsystemapi.exception.NotFoundException;
import com.example.managementsystemapi.mapper.ProductMapper;
import com.example.managementsystemapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductResponseDTO create(ProductRequestDTO dto) {

        log.info("Creating new product");

        Product product = mapper.toEntity(dto);

        return mapper.toDTO(repository.save(product));
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {

        log.info("Updating product - id: {}", id);

        Product product = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found - id: " + id));

        mapper.updateEntity(product, dto);

        return mapper.toDTO(repository.save(product));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findAll(Pageable pageable) {

        log.info("Fetching products - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<ProductResponseDTO> findOne(Long id) {

        log.info("Fetching product by id: {}", id);

        return repository.findById(id)
                .filter(Product::getActive)
                .map(mapper::toDTO);
    }

    public Product findOrThrow(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found - id: " + id));
    }

    public Optional<ProductResponseDTO> disable(Long id) {

        log.info("Disabling product - id: {}", id);

        return repository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    return repository.save(product);
                })
                .map(mapper::toDTO);
    }
}