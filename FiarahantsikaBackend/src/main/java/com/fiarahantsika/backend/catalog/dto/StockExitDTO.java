package com.fiarahantsika.backend.catalog.dto;

import java.time.Instant;

public record StockExitDTO(
        Long    id,
        Long    productId,
        Integer quantity,
        Instant exitDate,
        Integer remainingStock
) {}
