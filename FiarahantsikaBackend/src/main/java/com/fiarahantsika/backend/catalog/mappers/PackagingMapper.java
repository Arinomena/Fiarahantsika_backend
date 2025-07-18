package com.fiarahantsika.backend.catalog.mappers;

import com.fiarahantsika.backend.catalog.dto.PackagingDTO;
import com.fiarahantsika.backend.catalog.entities.Packaging;

public final class PackagingMapper {
    private PackagingMapper() {}

    public static PackagingDTO toDto(Packaging pkg) {
        return new PackagingDTO(
                pkg.getId(),
                pkg.getFormat(),
                pkg.getType(),
                pkg.getPrice(),
                pkg.getCurrentStock()
        );
    }

    public static Packaging toEntity(PackagingDTO dto) {
        Packaging pkg = new Packaging();
        pkg.setFormat(dto.format());
        pkg.setType(dto.type());
        pkg.setPrice(dto.price());
        pkg.setCurrentStock(dto.currentStock());
        return pkg;
    }

    public static void updateEntity(PackagingDTO dto, Packaging pkg) {
        pkg.setFormat(dto.format());
        pkg.setType(dto.type());
        pkg.setPrice(dto.price());
        pkg.setCurrentStock(dto.currentStock());
    }
}
