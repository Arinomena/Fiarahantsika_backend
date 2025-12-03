package com.fiarahantsika.backend.treasury.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TreasuryTransactionDTO(
        Long id,
        String type,
        Instant date,
        Long idUser,
        String description,
        String categorie,
        BigDecimal montant,
        BigDecimal balanceAfter
) {}
