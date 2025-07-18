package com.fiarahantsika.backend.catalog.dto;

public record CreateStockExitRequest(
        Long    productId,
        Integer quantity
) {}
