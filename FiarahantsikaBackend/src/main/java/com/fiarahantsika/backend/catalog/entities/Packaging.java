package com.fiarahantsika.backend.catalog.entities;

import com.fiarahantsika.backend.common.enums.PackagingFormat;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "packagings")
public class Packaging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PackagingFormat format;

    @Column(precision = 38, scale = 2)
    private BigDecimal price;

    @Column(name = "current_stock", nullable = false)
    private Integer currentStock = 0;

    public Packaging() {
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public PackagingFormat getFormat() {
        return format;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFormat(PackagingFormat format) {
        this.format = format;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }
}
