package com.fiarahantsika.backend.stats.dto;

import com.fiarahantsika.backend.common.enums.InvoiceStatus;
import java.math.BigDecimal;

public record InvoiceStatusDTO(InvoiceStatus status, Long count, BigDecimal totalAmount) {}
