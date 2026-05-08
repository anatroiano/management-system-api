package com.example.managementsystemapi.controller;

import com.example.managementsystemapi.dto.stock.*;
import com.example.managementsystemapi.enums.MovementType;
import com.example.managementsystemapi.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Stock", description = "Operations related to stock management and movements")
@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService service;

    @Operation(
            summary = "Create stock for a product",
            description = "Creates a stock entry for a product if it does not already exist"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock already exists or created successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping
    public ResponseEntity<StockResponseDTO> create(@RequestBody @Valid CreateStockRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createStockIfNotExists(request.getProductId()));
    }

    @Operation(
            summary = "Get stock by product",
            description = "Returns the current stock information for a given product"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock found"),
            @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    @GetMapping("/{productId}")
    public StockResponseDTO getByProduct(@Parameter(description = "Product ID") @PathVariable Long productId) {
        return service.getByProduct(productId);
    }

    @Operation(
            summary = "Get stock movement history",
            description = "Returns paginated stock movements for a product. Can be filtered by movement type"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product or stock not found")
    })
    @GetMapping("/{productId}/history")
    public Page<StockMovementResponseDTO> getHistory(
            @Parameter(description = "Product ID") @PathVariable Long productId,
            @Parameter(description = "Filter by movement type")
            @RequestParam(required = false) MovementType type,
            @ParameterObject
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable
    ) {
        return service.getHistory(productId, type, pageable);
    }

    @Operation(
            summary = "Register stock entry",
            description = "Adds quantity to stock and registers an ENTRY movement"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entry registered successfully"),
            @ApiResponse(responseCode = "404", description = "Product or stock not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/{productId}/entries")
    public StockMovementResponseDTO addEntry(
            @Parameter(description = "Product ID") @PathVariable Long productId,
            @RequestBody @Valid StockEntryRequestDTO request
    ) {
        return service.registerEntry(productId, request);
    }

    @Operation(
            summary = "Register manual stock exit",
            description = "Removes quantity from stock and registers a MANUAL_EXIT movement"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exit registered successfully"),
            @ApiResponse(responseCode = "404", description = "Product or stock not found"),
            @ApiResponse(responseCode = "422", description = "Insufficient stock"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/{productId}/exits")
    public StockMovementResponseDTO addExit(
            @Parameter(description = "Product ID") @PathVariable Long productId,
            @RequestBody @Valid StockExitRequestDTO request
    ) {
        return service.registerManualExit(productId, request);
    }
}