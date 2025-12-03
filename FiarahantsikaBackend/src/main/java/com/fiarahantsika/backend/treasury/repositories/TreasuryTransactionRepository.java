package com.fiarahantsika.backend.treasury.repositories;

import com.fiarahantsika.backend.treasury.entities.TreasuryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TreasuryTransactionRepository extends JpaRepository<TreasuryTransaction, Long> {
    Page<TreasuryTransaction> findAll(Pageable pageable);
}
