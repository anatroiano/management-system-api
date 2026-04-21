package com.example.managementsystemapi.repository;

import com.example.managementsystemapi.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
