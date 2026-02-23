package com.fiarahantsika.backend.catalog.mappers;

import com.fiarahantsika.backend.catalog.dto.ProductDTO;
import com.fiarahantsika.backend.catalog.entities.Product;

public final class ProductMapper {

    private ProductMapper() { /* utilitaire */ }

    public static ProductDTO toDto(Product p) {
        if (p == null) return null;
        return new ProductDTO(
                p.getId(),
                p.getAbbreviation(),
                p.getGroupType(),
                p.getName(),
                p.getFormat(),
                p.getPricePerUnit(),
                p.getPricePerGroup(),
                p.getQuantity(),
                p.getCurrentStock(),
                p.getPricePerUnitSupplier(),
                p.getPricePerGroupSupplier(),
                p.getGroupSize(),
                p.getSeuil(),
                p.getWeightKg(),
                p.getVolumeCl(),
                p.getCategorie()
        );
    }

    public static Product toEntity(ProductDTO dto) {
        if (dto == null) return null;
        Product p = new Product();
        updateEntity(dto, p);
        return p;
    }

    public static void updateEntity(ProductDTO dto, Product p) {
        if (dto == null || p == null) return;
        p.setAbbreviation(dto.abbreviation());
        p.setGroupType(dto.groupType());
        p.setName(dto.name());
        p.setFormat(dto.format());
        p.setPricePerUnit(dto.pricePerUnit());
        p.setPricePerGroup(dto.pricePerGroup());
        p.setQuantity(dto.quantity());
        p.setPricePerUnitSupplier(dto.pricePerUnitSupplier());
        p.setPricePerGroupSupplier(dto.pricePerGroupSupplier());
        p.setGroupSize(dto.groupSize());
        p.setSeuil(dto.seuil());
        p.setWeightKg(dto.weightKg());
        p.setVolumeCl(dto.volumeCl());
        p.setCategorie(dto.categorie());
    }
}