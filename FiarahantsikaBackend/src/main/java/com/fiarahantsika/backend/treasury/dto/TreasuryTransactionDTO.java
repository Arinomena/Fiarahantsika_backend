package com.fiarahantsika.backend.treasury.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TreasuryTransactionDTO(
        Long id,
        String type,          // CASH, Mobile, Banque, etc.
        Instant date,
        Long idUser,
        String description,
        String categorie,     // VENTE, SALAIRE, ACHAT, PAYMENT, etc.
        String direction,     // 🔹 ENTREE ou SORTIE
        BigDecimal montant,
        BigDecimal balanceAfter
) {}
