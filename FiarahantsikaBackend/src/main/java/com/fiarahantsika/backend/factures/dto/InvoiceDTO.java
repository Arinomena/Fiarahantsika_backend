package com.fiarahantsika.backend.factures.dto;

import com.fiarahantsika.backend.common.enums.InvoiceStatus;
import com.fiarahantsika.backend.orders.dto.OrderItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private Long id;
    private Long orderId;
    private BigDecimal totalAmount;
    private InvoiceStatus status;
    private LocalDate dueDate;
    private Instant createdAt;
    private BigDecimal paidAmount;
    private BigDecimal dueAmount;
}