package com.fiarahantsika.backend.treasury.mappers;

import com.fiarahantsika.backend.treasury.dto.TreasuryTransactionDTO;
import com.fiarahantsika.backend.treasury.entities.TreasuryTransaction;

public final class TreasuryTransactionMapper {

    private TreasuryTransactionMapper() {}

    public static TreasuryTransactionDTO toDto(TreasuryTransaction t) {
        return new TreasuryTransactionDTO(
                t.getId(),
                t.getType(),
                t.getDate(),
                t.getIdUser(),
                t.getDescription(),
                t.getCategorie(),
                t.getMontant(),
                t.getBalanceAfter()
        );
    }

    public static TreasuryTransaction toEntity(TreasuryTransactionDTO dto) {
        TreasuryTransaction t = new TreasuryTransaction();
        t.setType(dto.type());
        t.setDate(dto.date());
        t.setIdUser(dto.idUser());
        t.setDescription(dto.description());
        t.setCategorie(dto.categorie());
        t.setMontant(dto.montant());
        t.setBalanceAfter(dto.balanceAfter());
        return t;
    }

    public static void updateEntity(TreasuryTransactionDTO dto, TreasuryTransaction t) {
        t.setType(dto.type());
        t.setDate(dto.date());
        t.setIdUser(dto.idUser());
        t.setDescription(dto.description());
        t.setCategorie(dto.categorie());
        t.setMontant(dto.montant());
        t.setBalanceAfter(dto.balanceAfter());
    }
}
