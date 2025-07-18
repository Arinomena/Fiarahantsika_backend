package com.fiarahantsika.backend.orders.dto;

import com.fiarahantsika.backend.common.enums.ItemType;

public record CreateOrderItem(
        ItemType itemType,
        Long     itemId,
        Integer  quantity
){}