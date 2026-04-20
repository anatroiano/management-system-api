package com.example.managementsystemapi.repository;

import com.example.managementsystemapi.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
