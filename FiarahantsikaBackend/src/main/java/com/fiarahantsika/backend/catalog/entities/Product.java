package com.fiarahantsika.backend.catalog.entities;

import com.fiarahantsika.backend.common.enums.Format;
import com.fiarahantsika.backend.common.enums.GroupType;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String abbreviation;

    @Enumerated(EnumType.STRING)
    private Format format;

    @Enumerated(EnumType.STRING)
    @Column(name = "group_type", nullable = false)
    private GroupType groupType;

    private String name;

    @Column(name = "price_per_group", precision = 38, scale = 2)
    private BigDecimal pricePerGroup;

    @Column(name = "price_per_unit", precision = 38, scale = 2)
    private BigDecimal pricePerUnit;

    @Column(precision = 38, scale = 2)
    private BigDecimal quantity;

    @Column(name = "current_stock", nullable = false)
    private Integer currentStock = 0;

    @Column(name = "price_per_unit_supplier", precision = 10, scale = 2, nullable = false)
    private BigDecimal pricePerUnitSupplier = BigDecimal.ZERO;

    @Column(name = "price_per_group_supplier", precision = 10, scale = 2, nullable = false)
    private BigDecimal pricePerGroupSupplier = BigDecimal.ZERO;

    @Column(name = "group_size", nullable = false)
    private Integer groupSize = 1;

    @Column(name = "seuil", nullable = false)
    private Integer seuil = 0;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Format getFormat() {
        return format;
    }
    public void setFormat(Format format) {
        this.format = format;
    }

    public GroupType getGroupType() {
        return groupType;
    }
    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPricePerGroup() {
        return pricePerGroup;
    }
    public void setPricePerGroup(BigDecimal pricePerGroup) {
        this.pricePerGroup = pricePerGroup;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }
    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }
    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public BigDecimal getPricePerUnitSupplier() {
        return pricePerUnitSupplier;
    }
    public void setPricePerUnitSupplier(BigDecimal pricePerUnitSupplier) {
        this.pricePerUnitSupplier = pricePerUnitSupplier;
    }

    public BigDecimal getPricePerGroupSupplier() {
        return pricePerGroupSupplier;
    }
    public void setPricePerGroupSupplier(BigDecimal pricePerGroupSupplier) {
        this.pricePerGroupSupplier = pricePerGroupSupplier;
    }

    public Integer getGroupSize() {
        return groupSize;
    }
    public void setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
    }

    public Integer getSeuil() {
        return seuil;
    }
    public void setSeuil(Integer seuil) {
        this.seuil = seuil;
    }
}
