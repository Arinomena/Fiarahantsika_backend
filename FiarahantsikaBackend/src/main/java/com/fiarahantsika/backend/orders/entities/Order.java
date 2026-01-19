package com.fiarahantsika.backend.orders.entities;

import com.fiarahantsika.backend.clients.entities.Client;
import com.fiarahantsika.backend.common.enums.DestinationType;
import com.fiarahantsika.backend.users.entities.User;
import com.fiarahantsika.backend.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    private User user;

    @Setter
    @ManyToOne(optional = true)
    @JoinColumn(name = "client_id", nullable = true)
    private Client client;

    @Setter
    private Instant createdAt;

    @Setter
    private BigDecimal total;

    @Setter
    @Column(nullable = false)
    private boolean directSale;

    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DestinationType destination = DestinationType.CLIENT;

    // ✅ Champs supplémentaires pour correspondre au DTO
    @Setter
    @Column(name = "volume_cl", precision = 38, scale = 2, nullable = true)
    private BigDecimal volumeCl;

    @Setter
    @Column(name = "weight_kg", precision = 38, scale = 2, nullable = true)
    private BigDecimal weightKg;

    @Setter
    @Column(name = "emballage_fee", precision = 38, scale = 2, nullable = true)
    private BigDecimal emballageFee;

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
