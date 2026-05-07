package com.example.managementsystemapi.service;

import com.example.managementsystemapi.domain.Customer;
import com.example.managementsystemapi.dto.CustomerRequestDTO;
import com.example.managementsystemapi.dto.CustomerResponseDTO;
import com.example.managementsystemapi.exception.NotFoundException;
import com.example.managementsystemapi.mapper.CustomerMapper;
import com.example.managementsystemapi.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerService(final CustomerRepository repository,
                           final CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public CustomerResponseDTO create(final CustomerRequestDTO dto) {

        log.info("Creating new customer");

        final Customer customer = mapper.toEntity(dto);

        return mapper.toDTO(repository.save(customer));
    }

    public CustomerResponseDTO update(final Long id, final CustomerRequestDTO dto) {

        log.info("Updating customer - id: {}", id);

        final Customer customer = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found - id: " + id));

        mapper.updateEntity(customer, dto);

        return mapper.toDTO(repository.save(customer));
    }

    public Page<CustomerResponseDTO> findAll(final Pageable pageable) {

        log.info("Fetching customers - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    public Optional<CustomerResponseDTO> findOne(final Long id) {

        log.info("Fetching customer by id: {}", id);

        return repository.findById(id)
                .filter(Customer::getActive)
                .map(mapper::toDTO);
    }

    public Customer findOrThrow(final Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found - id: " + id));
    }

    public Optional<CustomerResponseDTO> disable(final Long id) {

        log.info("Disabling customer - id: {}", id);

        return repository.findById(id)
                .map(customer -> {
                    customer.setActive(false);
                    return repository.save(customer);
                })
                .map(mapper::toDTO);
    }
}