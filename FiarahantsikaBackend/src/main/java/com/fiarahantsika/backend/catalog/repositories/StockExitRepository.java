package com.fiarahantsika.backend.catalog.repositories;

import com.fiarahantsika.backend.catalog.entities.StockExit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StockExitRepository extends JpaRepository<StockExit, Long> {
    List<StockExit> findByProductId(Long productId);
    Page<StockExit> findByProductId(Long productId, Pageable pageable);
}
