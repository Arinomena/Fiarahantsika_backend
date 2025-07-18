package com.fiarahantsika.backend.catalog.repositories;

import com.fiarahantsika.backend.catalog.entities.PackagingExit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PackagingExitRepository extends JpaRepository<PackagingExit, Long> {
    List<PackagingExit> findByPackagingId(Long packagingId);
}
