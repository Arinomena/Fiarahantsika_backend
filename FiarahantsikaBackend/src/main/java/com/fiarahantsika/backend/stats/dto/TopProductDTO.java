package com.fiarahantsika.backend.stats.dto;

import java.math.BigDecimal;

public record TopProductDTO(Long productId, String productName, Long quantitySold, BigDecimal totalRevenue) {}
