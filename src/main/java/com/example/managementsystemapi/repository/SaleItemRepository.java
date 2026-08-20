package com.example.managementsystemapi.repository;

import com.example.managementsystemapi.domain.SaleItem;
import com.example.managementsystemapi.dto.dashboard.TopProductDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

    @Query("""
            SELECT new com.example.managementsystemapi.dto.dashboard.TopProductDTO(
                p.name,
                SUM(si.quantity),
                COALESCE(SUM(si.subtotal), 0)
            )
            FROM SaleItem si
            JOIN si.sale s
            JOIN si.product p
            WHERE s.active = true
              AND s.status != 'CANCELED'
              AND p.active = true
              AND s.createdAt >= :startDate
            GROUP BY p.id, p.name
            ORDER BY SUM(si.quantity) DESC
            """)
    List<TopProductDTO> findTopProducts(LocalDateTime startDate, Pageable pageable);

}