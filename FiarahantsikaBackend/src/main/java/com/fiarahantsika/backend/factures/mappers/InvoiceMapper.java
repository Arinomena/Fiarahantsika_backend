package com.fiarahantsika.backend.factures.mappers;

import com.fiarahantsika.backend.factures.dto.InvoiceDTO;
import com.fiarahantsika.backend.factures.entities.Invoice;

import java.math.BigDecimal;

public final class InvoiceMapper {

    private InvoiceMapper() {}

    public static InvoiceDTO toDto(Invoice i) {
        return new InvoiceDTO(
                i.getId(),
                i.getOrderId(),
                i.getTotalAmount(),
                i.getStatus(),
                i.getDueDate(),
                i.getCreatedAt(),
                i.getPayments().stream()
                        .map(p -> p.getAmount())
                        .reduce(BigDecimal.ZERO, BigDecimal::add),
                i.getTotalAmount().subtract(
                        i.getPayments().stream()
                                .map(p -> p.getAmount())
                                .reduce(BigDecimal.ZERO, BigDecimal::add))
        );
    }

    public static Invoice toEntity(InvoiceDTO dto) {
        Invoice i = new Invoice();
        i.setOrderId(dto.getOrderId());
        i.setTotalAmount(dto.getTotalAmount());
        i.setStatus(dto.getStatus());
        i.setDueDate(dto.getDueDate());
        return i;
    }

    public static void updateEntity(InvoiceDTO dto, Invoice i) {
        i.setOrderId(dto.getOrderId());
        i.setTotalAmount(dto.getTotalAmount());
        i.setStatus(dto.getStatus());
        i.setDueDate(dto.getDueDate());
    }
}
