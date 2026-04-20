package com.example.managementsystemapi.controller;

import com.example.managementsystemapi.dto.CustomerRequestDTO;
import com.example.managementsystemapi.dto.CustomerResponseDTO;
import com.example.managementsystemapi.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(final CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> create(@RequestBody @Valid final CustomerRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> update(@PathVariable final Long id,
                                                      @RequestBody @Valid final CustomerRequestDTO dto) {

        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping
    public ResponseEntity<Page<CustomerResponseDTO>> getAll(final Pageable pageable) {

        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getOne(@PathVariable final Long id) {

        return service.findOne(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<CustomerResponseDTO> disable(@PathVariable final Long id) {

        return service.disable(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}