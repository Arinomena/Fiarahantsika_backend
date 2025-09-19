package com.fiarahantsika.backend.catalog.repositories;

import com.fiarahantsika.backend.catalog.entities.PackagingEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PackagingEntryRepository extends JpaRepository<PackagingEntry, Long> {
    List<PackagingEntry> findByPackagingId(Long packagingId);
    Page<PackagingEntry> findByPackagingId(Long packagingId, Pageable pageable);
}
