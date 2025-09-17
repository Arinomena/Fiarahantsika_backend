package com.fiarahantsika.backend.stats.dto;

import java.math.BigDecimal;

public record SalesBySellerDTO(Long sellerId, String sellerName, Long ordersCount, BigDecimal totalSales) {}
