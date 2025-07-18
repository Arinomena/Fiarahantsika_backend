package com.fiarahantsika.backend.catalog.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "packaging_entries")
public class PackagingEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "packaging_id", nullable = false)
    private Packaging packaging;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "entry_date", nullable = false)
    private Instant entryDate = Instant.now();

    @Column(name = "remaining_stock", nullable = false)
    private Integer remainingStock;

    public PackagingEntry() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Packaging getPackaging() {
        return packaging;
    }

    public void setPackaging(Packaging packaging) {
        this.packaging = packaging;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Instant getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Instant entryDate) {
        this.entryDate = entryDate;
    }

    public Integer getRemainingStock() {
        return remainingStock;
    }

    public void setRemainingStock(Integer remainingStock) {
        this.remainingStock = remainingStock;
    }
}
