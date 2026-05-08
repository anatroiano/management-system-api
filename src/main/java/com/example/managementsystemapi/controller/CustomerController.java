package com.example.managementsystemapi.controller;

import com.example.managementsystemapi.dto.customer.CustomerRequestDTO;
import com.example.managementsystemapi.dto.customer.CustomerResponseDTO;
import com.example.managementsystemapi.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Customers", description = "Operations related to customer management")
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @Operation(summary = "Create a new customer", description = "Creates a new customer with the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> create(@RequestBody @Valid CustomerRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(summary = "Update a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> update(@PathVariable Long id,
                                                      @RequestBody @Valid CustomerRequestDTO dto) {

        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
            summary = "List customers",
            description = "Returns a paginated list of active customers"
    )
    @GetMapping
    public ResponseEntity<Page<CustomerResponseDTO>> getAll(@ParameterObject Pageable pageable) {

        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Operation(summary = "Get customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getOne(@PathVariable Long id) {

        return service.findOne(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Disable a customer", description = "Sets customer as inactive")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer disabled"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PatchMapping("/{id}/disable")
    public ResponseEntity<CustomerResponseDTO> disable(@PathVariable Long id) {

        return service.disable(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}