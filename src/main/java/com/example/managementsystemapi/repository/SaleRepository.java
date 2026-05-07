package com.example.managementsystemapi.repository;

import com.example.managementsystemapi.domain.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {

}