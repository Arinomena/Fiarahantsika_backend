package com.fiarahantsika.backend.orders.dto;

import java.math.BigDecimal;

public class OrderItemRequest {
    private Long itemId;
    private int bottleQty;
    private int crateQty;
    private BigDecimal unitPrice;
    private BigDecimal groupPrice;

    public Long getItemId() {
        return itemId;
    }
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public int getBottleQty() {
        return bottleQty;
    }
    public void setBottleQty(int bottleQty) {
        this.bottleQty = bottleQty;
    }

    public int getCrateQty() {
        return crateQty;
    }
    public void setCrateQty(int crateQty) {
        this.crateQty = crateQty;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getGroupPrice() {
        return groupPrice;
    }
    public void setGroupPrice(BigDecimal groupPrice) {
        this.groupPrice = groupPrice;
    }
}
