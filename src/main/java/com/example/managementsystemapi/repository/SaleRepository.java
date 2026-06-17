package com.example.managementsystemapi.repository;

import com.example.managementsystemapi.domain.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    Page<Sale> findByActiveIsTrue(Pageable pageable);

    @Query("SELECT COUNT(s) FROM Sale s")
    long countTotalSales();

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s")
    BigDecimal sumTotalRevenue();

}