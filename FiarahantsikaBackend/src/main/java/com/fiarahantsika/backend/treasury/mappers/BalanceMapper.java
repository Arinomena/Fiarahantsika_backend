package com.fiarahantsika.backend.treasury.mappers;

import com.fiarahantsika.backend.treasury.dto.BalanceDTO;
import com.fiarahantsika.backend.treasury.entities.Balance;

public final class BalanceMapper {
    private BalanceMapper() {}

    public static BalanceDTO toDto(Balance b) {
        return new BalanceDTO(
                b.getId(),
                b.getType(),
                b.getCurrentBalance(),
                b.getUpdatedAt()
        );
    }

    public static Balance toEntity(BalanceDTO dto) {
        Balance b = new Balance();
        b.setType(dto.type());
        b.setCurrentBalance(dto.currentBalance());
        b.setUpdatedAt(dto.updatedAt());
        return b;
    }

    public static void updateEntity(BalanceDTO dto, Balance b) {
        b.setType(dto.type());
        b.setCurrentBalance(dto.currentBalance());
        b.setUpdatedAt(dto.updatedAt());
    }
}
