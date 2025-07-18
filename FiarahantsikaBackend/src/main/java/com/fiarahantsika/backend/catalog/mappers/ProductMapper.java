package com.fiarahantsika.backend.catalog.mappers;

import com.fiarahantsika.backend.catalog.dto.ProductDTO;
import com.fiarahantsika.backend.catalog.entities.Product;

public final class ProductMapper {

    private ProductMapper() { /* utilitaire */ }

    public static ProductDTO toDto(Product p) {
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
                p.getSeuil()
        );
    }

    public static Product toEntity(ProductDTO dto) {
        Product p = new Product();
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
        return p;
    }

    public static void updateEntity(ProductDTO dto, Product p) {
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
    }
}
