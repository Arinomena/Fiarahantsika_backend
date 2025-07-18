package com.fiarahantsika.backend.catalog.repositories;

import com.fiarahantsika.backend.catalog.entities.PackagingEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PackagingEntryRepository extends JpaRepository<PackagingEntry, Long> {
    List<PackagingEntry> findByPackagingId(Long packagingId);
}
