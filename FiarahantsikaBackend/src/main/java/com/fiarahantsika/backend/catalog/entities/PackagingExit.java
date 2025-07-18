package com.fiarahantsika.backend.catalog.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "packaging_exits")
public class PackagingExit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "packaging_id", nullable = false)
    private Packaging packaging;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "exit_date", nullable = false)
    private Instant exitDate;

    @Column(name = "remaining_stock", nullable = false)
    private Integer remainingStock;

    // Getters / Setters
    public Long getId() { return id; }
    public Packaging getPackaging() { return packaging; }
    public Integer getQuantity() { return quantity; }
    public Instant getExitDate() { return exitDate; }
    public Integer getRemainingStock() { return remainingStock; }


    public void setId(Long id) { this.id = id; }
    public void setPackaging(Packaging packaging) { this.packaging = packaging; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setExitDate(Instant exitDate) { this.exitDate = exitDate; }
    public void setRemainingStock(Integer remainingStock) {
        this.remainingStock = remainingStock;
    }
}
