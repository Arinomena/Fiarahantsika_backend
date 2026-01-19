package com.fiarahantsika.backend.factures.mappers;

import com.fiarahantsika.backend.factures.dto.InvoiceDTO;
import com.fiarahantsika.backend.factures.entities.Invoice;

import java.math.BigDecimal;

public final class InvoiceMapper {

    private InvoiceMapper() {}

    public static InvoiceDTO toDto(Invoice i) {
        BigDecimal paid = i.getPayments().stream()
                .map(p -> p.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal due = i.getTotalAmount().subtract(paid);

        return new InvoiceDTO(
                i.getId(),
                i.getOrderId(),
                i.getTotalAmount(),
                i.getStatus(),
                i.getDueDate(),
                i.getCreatedAt(),
                paid,
                due,
                i.getDestination()
        );
    }

    public static Invoice toEntity(InvoiceDTO dto) {
        Invoice i = new Invoice();
        i.setOrderId(dto.getOrderId());
        i.setTotalAmount(dto.getTotalAmount());
        i.setStatus(dto.getStatus());
        i.setDueDate(dto.getDueDate());
        i.setDestination(dto.getDestination());
        return i;
    }

    public static void updateEntity(InvoiceDTO dto, Invoice i) {
        i.setOrderId(dto.getOrderId());
        i.setTotalAmount(dto.getTotalAmount());
        i.setStatus(dto.getStatus());
        i.setDueDate(dto.getDueDate());
        i.setDestination(dto.getDestination());
    }
}
