package com.fiarahantsika.backend.catalog.dto;

import java.time.Instant;

public record PackagingEntryDTO(
        Long      id,
        Long      packagingId,
        Integer   quantity,
        Instant   entryDate,
        Integer   remainingStock
) {}
