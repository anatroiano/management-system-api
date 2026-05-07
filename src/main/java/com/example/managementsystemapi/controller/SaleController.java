package com.example.managementsystemapi.controller;

import com.example.managementsystemapi.dto.CreateSaleRequestDTO;
import com.example.managementsystemapi.dto.SaleResponseDTO;
import com.example.managementsystemapi.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Tag(name = "Sales")
public class SaleController {

    private final SaleService service;

    @Operation(
            summary = "Create a sale",
            description = "Creates a new sale and automatically updates stock quantities"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale created successfully"),
            @ApiResponse(responseCode = "404", description = "Customer or product not found"),
            @ApiResponse(responseCode = "400", description = "Insufficient stock")
    })
    @PostMapping
    public SaleResponseDTO create(@RequestBody @Valid CreateSaleRequestDTO request) {
        return service.create(request);
    }

    @Operation(
            summary = "Find sale by ID",
            description = "Returns a sale by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale found"),
            @ApiResponse(responseCode = "404", description = "Sale not found")
    })
    @GetMapping("/{id}")
    public SaleResponseDTO findOne(@PathVariable Long id) {
        return service.findOne(id);
    }

    @Operation(
            summary = "List all sales",
            description = "Returns a paginated list of sales"
    )
    @ApiResponse(responseCode = "200", description = "Sales retrieved successfully")
    @GetMapping
    public ResponseEntity<Page<SaleResponseDTO>> getAll(final @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Operation(
            summary = "Cancel a sale",
            description = "Cancels a sale and restores stock quantities"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale canceled successfully"),
            @ApiResponse(responseCode = "404", description = "Sale not found"),
            @ApiResponse(responseCode = "400", description = "Sale already canceled")
    })
    @PatchMapping("/{id}/cancel")
    public SaleResponseDTO cancel(@PathVariable Long id) {
        return service.cancel(id);
    }

}