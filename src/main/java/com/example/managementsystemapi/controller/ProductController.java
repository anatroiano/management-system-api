package com.example.managementsystemapi.controller;

import com.example.managementsystemapi.dto.ProductRequestDTO;
import com.example.managementsystemapi.dto.ProductResponseDTO;
import com.example.managementsystemapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Products", description = "Operations related to product management")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(final ProductService service) {
        this.service = service;
    }

    @Operation(summary = "Create a new product", description = "Creates a new product with the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid final ProductRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(summary = "Update a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable final Long id,
                                                     @RequestBody @Valid final ProductRequestDTO dto) {

        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
            summary = "List products",
            description = "Returns a paginated list of active products"
    )
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAll(final @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Operation(summary = "Get product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getOne(@PathVariable final Long id) {

        return service.findOne(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Disable a product", description = "Sets product as inactive")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product disabled"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PatchMapping("/{id}/disable")
    public ResponseEntity<ProductResponseDTO> disable(@PathVariable final Long id) {

        return service.disable(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}