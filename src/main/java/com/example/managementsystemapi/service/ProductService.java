package com.example.managementsystemapi.service;

import com.example.managementsystemapi.domain.Product;
import com.example.managementsystemapi.dto.ProductRequestDTO;
import com.example.managementsystemapi.dto.ProductResponseDTO;
import com.example.managementsystemapi.exception.NotFoundException;
import com.example.managementsystemapi.mapper.ProductMapper;
import com.example.managementsystemapi.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductService(final ProductRepository repository,
                          final ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProductResponseDTO create(final ProductRequestDTO dto) {

        log.info("Creating new product");

        final Product product = mapper.toEntity(dto);

        return mapper.toDTO(repository.save(product));
    }

    public ProductResponseDTO update(final Long id, final ProductRequestDTO dto) {

        log.info("Updating product - id: {}", id);

        final Product product = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found - id: " + id));

        mapper.updateEntity(product, dto);

        return mapper.toDTO(repository.save(product));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findAll(final Pageable pageable) {

        log.info("Fetching products - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<ProductResponseDTO> findOne(final Long id) {

        log.info("Fetching product by id: {}", id);

        return repository.findById(id)
                .filter(Product::getActive)
                .map(mapper::toDTO);
    }

    public Product findOrThrow(final Long productId) {

        return repository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found - id: " + productId));
    }

    public Optional<ProductResponseDTO> disable(final Long id) {

        log.info("Disabling product - id: {}", id);

        return repository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    return repository.save(product);
                })
                .map(mapper::toDTO);
    }
}