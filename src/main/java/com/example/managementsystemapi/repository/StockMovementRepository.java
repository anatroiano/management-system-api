package com.example.managementsystemapi.repository;

import com.example.managementsystemapi.domain.StockMovement;
import com.example.managementsystemapi.enums.MovementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    Page<StockMovement> findByProductIdOrderByCreatedAtDesc(Long productId, Pageable pageable);

    Page<StockMovement> findByProductIdAndTypeOrderByCreatedAtDesc(Long productId, MovementType type, Pageable pageable);
}
 