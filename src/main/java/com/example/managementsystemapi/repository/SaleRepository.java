package com.example.managementsystemapi.repository;

import com.example.managementsystemapi.domain.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    Page<Sale> findByActiveIsTrue(Pageable pageable);

}