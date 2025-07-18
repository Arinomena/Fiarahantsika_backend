package com.fiarahantsika.backend.catalog.dto;

public record CreatePackagingEntryRequest(
        Long    packagingId,
        Integer quantity
) {}
