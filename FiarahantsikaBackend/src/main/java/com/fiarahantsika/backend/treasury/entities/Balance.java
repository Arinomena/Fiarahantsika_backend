package com.fiarahantsika.backend.treasury.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "balances")
public class Balance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // CASH, CARD, TRANSFER, GLOBAL
    private BigDecimal currentBalance;
    private Instant updatedAt;

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public BigDecimal getCurrentBalance() { return currentBalance; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
