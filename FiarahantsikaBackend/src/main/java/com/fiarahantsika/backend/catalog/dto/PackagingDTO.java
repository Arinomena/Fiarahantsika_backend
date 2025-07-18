package com.fiarahantsika.backend.catalog.dto;

import com.fiarahantsika.backend.common.enums.Format;
import com.fiarahantsika.backend.common.enums.PackagingFormat;

import java.math.BigDecimal;

public record PackagingDTO(
        Long id,
        PackagingFormat format,
        String type,
        BigDecimal price,
        Integer currentStock
) {}
