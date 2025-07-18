package com.fiarahantsika.backend.catalog.repositories;

import com.fiarahantsika.backend.catalog.entities.StockAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockAlertRepository extends JpaRepository<StockAlert, Long> {
    List<StockAlert> findByResolvedFalse();
    boolean existsByProductIdAndResolvedFalse(Long productId);
}