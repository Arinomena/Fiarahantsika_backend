package com.fiarahantsika.backend.catalog.dto;

public record CreatePackagingExitRequest(
        Long    packagingId,
        Integer quantity
) {}
