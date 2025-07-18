package com.fiarahantsika.backend.catalog.dto;

public record StockAlertDTO(
        Long    productId,
        String  name,
        Integer currentStock,
        Integer seuil
) {}