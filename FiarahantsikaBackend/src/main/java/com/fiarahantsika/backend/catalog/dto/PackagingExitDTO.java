package com.fiarahantsika.backend.catalog.dto;

import java.time.Instant;

public record PackagingExitDTO(
        Long      id,
        Long      packagingId,
        Integer   quantity,
        Instant   exitDate,
        Integer   remainingStock
) {}
