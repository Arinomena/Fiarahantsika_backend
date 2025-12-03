package com.fiarahantsika.backend.treasury.services;

import com.fiarahantsika.backend.treasury.dto.TreasuryTransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITreasuryTransactionService {
    List<TreasuryTransactionDTO> getAllTransactions();
    TreasuryTransactionDTO getTransactionById(Long id);
    TreasuryTransactionDTO createTransaction(TreasuryTransactionDTO dto);
    TreasuryTransactionDTO updateTransaction(Long id, TreasuryTransactionDTO dto);
    void deleteTransaction(Long id);
    Page<TreasuryTransactionDTO> getTransactionsPage(Pageable pageable);
}
