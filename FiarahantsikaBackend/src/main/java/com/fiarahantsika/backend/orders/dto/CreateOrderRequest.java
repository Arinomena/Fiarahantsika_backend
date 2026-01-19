package com.fiarahantsika.backend.orders.dto;

import com.fiarahantsika.backend.common.enums.DestinationType;
import com.fiarahantsika.backend.common.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public class CreateOrderRequest {
    private Long clientId;
    private boolean directSale;
    private DestinationType destination;
    private OrderStatus status;
    private BigDecimal volumeCl;
    private BigDecimal weightKg;
    private BigDecimal emballageFee;
    private BigDecimal total; // ✅ ajouté
    private List<OrderItemRequest> items;

    public Long getClientId() {
        return clientId;
    }
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public boolean isDirectSale() {
        return directSale;
    }
    public void setDirectSale(boolean directSale) {
        this.directSale = directSale;
    }

    public DestinationType getDestination() {
        return destination;
    }
    public void setDestination(DestinationType destination) {
        this.destination = destination;
    }

    public OrderStatus getStatus() {
        return status;
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getVolumeCl() {
        return volumeCl;
    }
    public void setVolumeCl(BigDecimal volumeCl) {
        this.volumeCl = volumeCl;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }
    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public BigDecimal getEmballageFee() {
        return emballageFee;
    }
    public void setEmballageFee(BigDecimal emballageFee) {
        this.emballageFee = emballageFee;
    }

    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }
    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}
