package com.fiarahantsika.backend.catalog.dto;

import com.fiarahantsika.backend.common.enums.Format;
import com.fiarahantsika.backend.common.enums.GroupType;

import java.math.BigDecimal;

public record ProductDTO(
        Long      id,
        String    abbreviation,
        GroupType groupType,
        String    name,
        Format    format,
        BigDecimal pricePerUnit,
        BigDecimal pricePerGroup,
        BigDecimal quantity,
        Integer   currentStock,
        BigDecimal pricePerUnitSupplier,
        BigDecimal pricePerGroupSupplier,
        Integer   groupSize,
        Integer   seuil
) {}
