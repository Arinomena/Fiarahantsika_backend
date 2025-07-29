package com.fiarahantsika.backend.factures.mappers;

import com.fiarahantsika.backend.factures.dto.PaymentDTO;
import com.fiarahantsika.backend.factures.entities.Invoice;
import com.fiarahantsika.backend.factures.entities.Payment;

public final class PaymentMapper {

    private PaymentMapper() {}

    public static PaymentDTO toDto(Payment p) {
        return new PaymentDTO(
                p.getId(),
                p.getInvoice().getId(),
                p.getAmount(),
                p.getPaidAt()
        );
    }

    public static Payment toEntity(PaymentDTO dto, Invoice invoice) {
        Payment p = new Payment();
        p.setInvoice(invoice);
        p.setAmount(dto.getAmount());
        p.setPaidAt(dto.getPaidAt());
        return p;
    }

    public static void updateEntity(PaymentDTO dto, Payment p) {
        p.setAmount(dto.getAmount());
        p.setPaidAt(dto.getPaidAt());
    }
}
