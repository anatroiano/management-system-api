package com.example.managementsystemapi.repository;

import com.example.managementsystemapi.domain.Sale;
import com.example.managementsystemapi.dto.dashboard.RecentSaleDTO;
import com.example.managementsystemapi.dto.dashboard.SalesByDayDTO;
import com.example.managementsystemapi.dto.dashboard.TopCustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    Page<Sale> findByActiveIsTrue(Pageable pageable);

    long countByActiveIsTrue();

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE s.active = true AND s.status != 'CANCELED'")
    BigDecimal sumTotalRevenue();

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.active = true AND s.status != 'CANCELED' AND s.createdAt >= :startDate AND s.createdAt < :endDate")
    long countSalesBetweenDates(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE s.active = true AND s.status != 'CANCELED' AND s.createdAt >= :startDate AND s.createdAt < :endDate")
    BigDecimal sumRevenueBetweenDates(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);

    @Query("""
            SELECT new com.example.managementsystemapi.dto.dashboard.SalesByDayDTO(
                CAST(s.createdAt AS LocalDate),
                COUNT(s),
                COALESCE(SUM(s.totalAmount), 0)
            )
            FROM Sale s
            WHERE s.active = true
              AND s.status != 'CANCELED'
              AND s.createdAt >= :startDate
            GROUP BY FUNCTION('DATE', s.createdAt)
            ORDER BY FUNCTION('DATE', s.createdAt) ASC
            """)
    List<SalesByDayDTO> findSalesByDay(LocalDateTime startDate);

    @Query("""
            SELECT new com.example.managementsystemapi.dto.dashboard.RecentSaleDTO(
                s.id,
                s.customer.name,
                s.totalAmount,
                s.createdAt,
                s.status
            )
            FROM Sale s
            WHERE s.active = true
            ORDER BY s.createdAt DESC
            """)
    List<RecentSaleDTO> findRecentSales(Pageable pageable);

    @Query("""
            SELECT new com.example.managementsystemapi.dto.dashboard.TopCustomerDTO(
                c.id,
                c.name,
                COUNT(s),
                COALESCE(SUM(s.totalAmount), 0)
            )
            FROM Sale s
            JOIN s.customer c
            WHERE s.active = true
              AND s.status != 'CANCELED'
              AND c.active = true
              AND s.createdAt >= :startDate
            GROUP BY c.id, c.name
            ORDER BY SUM(s.totalAmount) DESC
            """)
    List<TopCustomerDTO> findTopCustomers(LocalDateTime startDate, Pageable pageable);
}