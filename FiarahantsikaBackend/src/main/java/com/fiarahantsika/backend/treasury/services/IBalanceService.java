package com.fiarahantsika.backend.treasury.services;

import com.fiarahantsika.backend.treasury.dto.BalanceDTO;

import java.util.List;

public interface IBalanceService {
    List<BalanceDTO> getAllBalances();
    BalanceDTO getBalanceById(Long id);
    BalanceDTO getBalanceByType(String type);
    BalanceDTO createOrUpdateBalance(BalanceDTO dto);
    void deleteBalance(Long id);
    Double getGlobalBalance();
}
