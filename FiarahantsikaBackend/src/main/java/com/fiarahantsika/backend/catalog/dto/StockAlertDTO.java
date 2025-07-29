package com.fiarahantsika.backend.catalog.dto;

import java.time.Instant;

public record StockAlertDTO(
        Long    id,
        Long    productId,
        String  name,
        Integer currentStock,
        Integer seuil,
        Instant createdAt,
        Boolean resolved,
        Instant resolvedAt
) {}