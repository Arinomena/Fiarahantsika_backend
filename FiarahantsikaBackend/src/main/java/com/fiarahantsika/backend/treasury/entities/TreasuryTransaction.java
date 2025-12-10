package com.fiarahantsika.backend.treasury.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "treasury_transactions")
public class TreasuryTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private Instant date;
    private Long idUser;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String categorie;

    private String direction;

    private BigDecimal montant;
    private BigDecimal balanceAfter;

    // --- Getters ---
    public Long getId() { return id; }
    public String getType() { return type; }
    public Instant getDate() { return date; }
    public Long getIdUser() { return idUser; }
    public String getDescription() { return description; }
    public String getCategorie() { return categorie; }
    public String getDirection() { return direction; }
    public BigDecimal getMontant() { return montant; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }

    // --- Setters ---
    public void setId(Long id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setDate(Instant date) { this.date = date; }
    public void setIdUser(Long idUser) { this.idUser = idUser; }
    public void setDescription(String description) { this.description = description; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public void setDirection(String direction) { this.direction = direction; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    public void setBalanceAfter(BigDecimal balanceAfter) { this.balanceAfter = balanceAfter; }
}
