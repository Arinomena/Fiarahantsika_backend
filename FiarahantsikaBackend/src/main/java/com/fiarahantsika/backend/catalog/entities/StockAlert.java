package com.fiarahantsika.backend.catalog.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "stock_alerts")
public class StockAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Boolean resolved = false;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    // Getters & setters

    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public Instant getCreatedAt() { return createdAt; }
    public Boolean getResolved() { return resolved; }
    public Instant getResolvedAt() { return resolvedAt; }

    public void setId(Long id) { this.id = id; }
    public void setProduct(Product product) { this.product = product; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setResolved(Boolean resolved) { this.resolved = resolved; }
    public void setResolvedAt(Instant resolvedAt) { this.resolvedAt = resolvedAt; }
}
