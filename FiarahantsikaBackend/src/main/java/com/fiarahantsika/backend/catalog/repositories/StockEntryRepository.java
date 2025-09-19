package com.fiarahantsika.backend.catalog.repositories;

import com.fiarahantsika.backend.catalog.entities.StockEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StockEntryRepository extends JpaRepository<StockEntry, Long> {
    List<StockEntry> findByProductId(Long productId);
    Page<StockEntry> findByProductId(Long productId, Pageable pageable);
}
