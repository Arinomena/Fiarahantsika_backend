package com.fiarahantsika.backend.catalog.dto;

public record CreateStockEntryRequest(
        Long    productId,
        Integer quantity
) {}
