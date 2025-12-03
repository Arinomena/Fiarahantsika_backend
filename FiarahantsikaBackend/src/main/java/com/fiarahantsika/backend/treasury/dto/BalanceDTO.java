package com.fiarahantsika.backend.treasury.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record BalanceDTO(
        Long id,
        String type,
        BigDecimal currentBalance,
        Instant updatedAt
) {}
