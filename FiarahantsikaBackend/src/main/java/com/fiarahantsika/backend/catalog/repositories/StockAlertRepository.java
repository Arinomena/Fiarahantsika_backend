package com.fiarahantsika.backend.catalog.repositories;

import com.fiarahantsika.backend.catalog.entities.StockAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StockAlertRepository extends JpaRepository<StockAlert, Long> {
    List<StockAlert> findByResolvedFalse();
    List<StockAlert> findByResolvedTrue();
    boolean existsByProductIdAndResolvedFalse(Long productId);
    Optional<StockAlert> findByProductId(Long productId);
}