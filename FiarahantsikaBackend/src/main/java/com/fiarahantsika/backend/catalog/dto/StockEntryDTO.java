package com.fiarahantsika.backend.catalog.dto;

import java.time.Instant;

public record StockEntryDTO(
        Long    id,
        Long    productId,
        Integer groupQty,
        Integer bottleQty,
        Instant entryDate,
        Integer remainingStock
) {}
