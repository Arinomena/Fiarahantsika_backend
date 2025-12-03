package com.fiarahantsika.backend.treasury.services;

import com.fiarahantsika.backend.treasury.dto.TreasuryTransactionDTO;
import com.fiarahantsika.backend.treasury.entities.TreasuryTransaction;
import com.fiarahantsika.backend.treasury.mappers.TreasuryTransactionMapper;
import com.fiarahantsika.backend.treasury.repositories.TreasuryTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TreasuryTransactionServiceImpl implements ITreasuryTransactionService {

    private final TreasuryTransactionRepository repo;

    @Override
    @Transactional(readOnly = true)
    public List<TreasuryTransactionDTO> getAllTransactions() {
        return repo.findAll().stream()
                .map(TreasuryTransactionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TreasuryTransactionDTO getTransactionById(Long id) {
        TreasuryTransaction t = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction non trouvée : " + id));
        return TreasuryTransactionMapper.toDto(t);
    }

    @Override
    public TreasuryTransactionDTO createTransaction(TreasuryTransactionDTO dto) {
        TreasuryTransaction t = TreasuryTransactionMapper.toEntity(dto);
        TreasuryTransaction saved = repo.save(t);
        return TreasuryTransactionMapper.toDto(saved);
    }

    @Override
    public TreasuryTransactionDTO updateTransaction(Long id, TreasuryTransactionDTO dto) {
        TreasuryTransaction t = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction non trouvée : " + id));
        TreasuryTransactionMapper.updateEntity(dto, t);
        TreasuryTransaction updated = repo.save(t);
        return TreasuryTransactionMapper.toDto(updated);
    }

    @Override
    public void deleteTransaction(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TreasuryTransactionDTO> getTransactionsPage(Pageable pageable) {
        return repo.findAll(pageable).map(TreasuryTransactionMapper::toDto);
    }
}
