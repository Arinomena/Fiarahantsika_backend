package com.fiarahantsika.backend.catalog.repositories;

import com.fiarahantsika.backend.catalog.entities.PackagingExit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PackagingExitRepository extends JpaRepository<PackagingExit, Long> {
    List<PackagingExit> findByPackagingId(Long packagingId);
    Page<PackagingExit> findByPackagingId(Long packagingId, Pageable pageable);
}
