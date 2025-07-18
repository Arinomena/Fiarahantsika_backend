package com.fiarahantsika.backend.orders.dto;

import java.util.List;

public class CreateOrderRequest {
    private Long clientId;
    private boolean directSale;
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

    public List<OrderItemRequest> getItems() {
        return items;
    }
    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}
