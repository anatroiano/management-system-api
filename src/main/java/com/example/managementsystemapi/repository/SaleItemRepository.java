package com.example.managementsystemapi.repository;

import com.example.managementsystemapi.domain.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

}