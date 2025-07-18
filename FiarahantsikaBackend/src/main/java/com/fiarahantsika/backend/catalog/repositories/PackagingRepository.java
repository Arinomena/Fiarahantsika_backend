package com.fiarahantsika.backend.catalog.repositories;

import com.fiarahantsika.backend.catalog.entities.Packaging;
import com.fiarahantsika.backend.common.enums.PackagingFormat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackagingRepository extends JpaRepository<Packaging,Long> {
    Optional<Packaging> findByFormatAndType(PackagingFormat format, String type);
}
