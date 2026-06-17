package com.example.managementsystemapi.repository;

import com.example.managementsystemapi.domain.Stock;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Page<Stock> findByActiveIsTrue(Pageable pageable);

    Optional<Stock> findByProductId(Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Stock s WHERE s.product.id = :productId")
    Optional<Stock> findByProductIdForUpdate(Long productId);

    @Query("SELECT COUNT(s) FROM Stock s WHERE s.active = true")
    long countActive();

    @Query("SELECT COUNT(s) FROM Stock s WHERE s.active = true AND s.quantity > 0 AND s.quantity < 100")
    long countLowStock();

    @Query("SELECT COUNT(s) FROM Stock s WHERE s.active = true AND s.quantity = 0")
    long countOutOfStock();

}