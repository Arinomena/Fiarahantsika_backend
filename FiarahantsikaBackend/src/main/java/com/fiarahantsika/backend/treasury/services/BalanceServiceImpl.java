package com.fiarahantsika.backend.treasury.services;

import com.fiarahantsika.backend.treasury.dto.BalanceDTO;
import com.fiarahantsika.backend.treasury.entities.Balance;
import com.fiarahantsika.backend.treasury.mappers.BalanceMapper;
import com.fiarahantsika.backend.treasury.repositories.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BalanceServiceImpl implements IBalanceService {

    private final BalanceRepository repo;

    @Override
    @Transactional(readOnly = true)
    public List<BalanceDTO> getAllBalances() {
        return repo.findAll().stream().map(BalanceMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BalanceDTO getBalanceById(Long id) {
        Balance b = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Balance non trouvée : " + id));
        return BalanceMapper.toDto(b);
    }

    @Override
    @Transactional(readOnly = true)
    public BalanceDTO getBalanceByType(String type) {
        Balance b = repo.findByType(type)
                .orElseThrow(() -> new IllegalArgumentException("Balance non trouvée pour type : " + type));
        return BalanceMapper.toDto(b);
    }

    @Override
    public BalanceDTO createOrUpdateBalance(BalanceDTO dto) {
        Balance b = repo.findByType(dto.type()).orElseGet(() -> {
            Balance newBalance = new Balance();
            newBalance.setType(dto.type());
            newBalance.setCurrentBalance(BigDecimal.ZERO);
            return newBalance;
        });
        BigDecimal newValue = b.getCurrentBalance().add(dto.currentBalance() != null ? dto.currentBalance() : BigDecimal.ZERO);
        b.setCurrentBalance(newValue);
        Balance saved = repo.save(b);
        return BalanceMapper.toDto(saved);
    }

    @Override
    public void deleteBalance(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getGlobalBalance() {
        return repo.findAll().stream()
                .map(Balance::getCurrentBalance)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
    }
}
