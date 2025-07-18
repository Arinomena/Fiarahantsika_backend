package com.fiarahantsika.backend.catalog.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "stock_exits")
public class StockExit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "exit_date", nullable = false)
    private Instant exitDate = Instant.now();

    @Column(name = "remaining_stock", nullable = false)
    private Integer remainingStock;

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Instant getExitDate() { return exitDate; }
    public void setExitDate(Instant exitDate) { this.exitDate = exitDate; }

    public Integer getRemainingStock() { return remainingStock; }
    public void setRemainingStock(Integer remainingStock) {
        this.remainingStock = remainingStock;
    }
}
