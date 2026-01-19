package com.fiarahantsika.backend.orders.dto;

import com.fiarahantsika.backend.common.enums.ItemType;
import java.math.BigDecimal;

public record CreateOrderItem(
        ItemType itemType,
        Long     itemId,
        Integer  quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal
) {}
