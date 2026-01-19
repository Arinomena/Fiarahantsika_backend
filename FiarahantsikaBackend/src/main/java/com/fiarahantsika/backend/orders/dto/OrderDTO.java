package com.fiarahantsika.backend.orders.dto;

import com.fiarahantsika.backend.common.enums.DestinationType;
import com.fiarahantsika.backend.common.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderDTO(
        Long id,
        String    username,
        Long      clientId,
        Instant createdAt,
        BigDecimal total,
        OrderStatus status,
        DestinationType destination,
        BigDecimal volumeCl,
        BigDecimal weightKg,
        BigDecimal emballageFee,
        List<OrderItemDTO> items
){}
