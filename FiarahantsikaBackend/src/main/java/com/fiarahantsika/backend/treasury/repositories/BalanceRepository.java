package com.fiarahantsika.backend.treasury.repositories;

import com.fiarahantsika.backend.treasury.entities.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findByType(String type);
}
