package com.fiarahantsika.backend.orders.entities;

import com.fiarahantsika.backend.clients.entities.Client;
import com.fiarahantsika.backend.users.entities.User;
import com.fiarahantsika.backend.common.enums.OrderStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = true)
    @JoinColumn(name = "client_id", nullable = true)
    private Client client;

    private Instant createdAt;

    private BigDecimal total;

    @Column(nullable = false)
    private boolean directSale;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Client getClient() {
        return client;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public boolean isDirectSale() {
        return directSale;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return items;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setDirectSale(boolean directSale) {
        this.directSale = directSale;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setItems(List<OrderItem> newItems) {

        this.items.clear();
        if (newItems != null) {
            for (OrderItem i : newItems) {
                i.setOrder(this);
                this.items.add(i);
            }
        }
    }
}
