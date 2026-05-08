package com.example.managementsystemapi.service;

import com.example.managementsystemapi.domain.Customer;
import com.example.managementsystemapi.dto.customer.CustomerRequestDTO;
import com.example.managementsystemapi.dto.customer.CustomerResponseDTO;
import com.example.managementsystemapi.exception.NotFoundException;
import com.example.managementsystemapi.mapper.CustomerMapper;
import com.example.managementsystemapi.repository.CustomerRepository;
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
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerResponseDTO create(CustomerRequestDTO dto) {

        log.info("Creating new customer");

        Customer customer = mapper.toEntity(dto);

        return mapper.toDTO(repository.save(customer));
    }

    public CustomerResponseDTO update(Long id, CustomerRequestDTO dto) {

        log.info("Updating customer - id: {}", id);

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found - id: " + id));

        mapper.updateEntity(customer, dto);

        return mapper.toDTO(repository.save(customer));
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponseDTO> findAll(Pageable pageable) {

        log.info("Fetching customers - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<CustomerResponseDTO> findOne(Long id) {

        log.info("Fetching customer by id: {}", id);

        return repository.findById(id)
                .filter(Customer::getActive)
                .map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Customer findOrThrow(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found - id: " + id));
    }

    public Optional<CustomerResponseDTO> disable(Long id) {

        log.info("Disabling customer - id: {}", id);

        return repository.findById(id)
                .map(customer -> {
                    customer.setActive(false);
                    return repository.save(customer);
                })
                .map(mapper::toDTO);
    }
}