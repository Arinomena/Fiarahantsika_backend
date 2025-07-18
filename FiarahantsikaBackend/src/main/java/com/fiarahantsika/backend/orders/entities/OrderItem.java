package com.fiarahantsika.backend.orders.entities;

import com.fiarahantsika.backend.common.enums.ItemType;
import jakarta.persistence.*;

        import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private Long itemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public Long getItemId() {
        return itemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }
}
