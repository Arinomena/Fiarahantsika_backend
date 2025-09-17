package com.fiarahantsika.backend.stats.dto;

import java.math.BigDecimal;

public record SalesByClientDTO(Long clientId, String clientName, BigDecimal totalSales) {}
