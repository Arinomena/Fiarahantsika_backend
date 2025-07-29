package com.fiarahantsika.backend.factures.services;

import com.fiarahantsika.backend.common.enums.InvoiceStatus;
import com.fiarahantsika.backend.factures.dto.InvoiceDTO;
import com.fiarahantsika.backend.factures.dto.PaymentDTO;
import com.fiarahantsika.backend.factures.entities.Invoice;
import com.fiarahantsika.backend.factures.entities.Payment;
import com.fiarahantsika.backend.factures.mappers.InvoiceMapper;
import com.fiarahantsika.backend.factures.mappers.PaymentMapper;
import com.fiarahantsika.backend.factures.repositories.InvoiceRepository;
import com.fiarahantsika.backend.factures.repositories.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
class InvoiceServiceImpl implements IInvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;


    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDTO> listAll() {
        return invoiceRepository.findAll().stream()
                .map(InvoiceMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public InvoiceDTO create(InvoiceDTO req) {
        Invoice inv = new Invoice();
        inv.setOrderId(req.getOrderId());
        inv.setTotalAmount(req.getTotalAmount());
        inv.setStatus(InvoiceStatus.EMISE);
        inv.setDueDate(req.getDueDate());
        inv.setCreatedAt(Instant.now());
        Invoice saved = invoiceRepository.save(inv);

        BigDecimal paid = BigDecimal.ZERO;
        BigDecimal due  = saved.getTotalAmount();

        return new InvoiceDTO(
                saved.getId(),
                saved.getOrderId(),
                saved.getTotalAmount(),
                saved.getStatus(),
                saved.getDueDate(),
                saved.getCreatedAt(),
                paid,
                due
        );
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceDTO getById(Long id) {
        Invoice inv = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found: " + id));

        BigDecimal paid = inv.getPayments().stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal due = inv.getTotalAmount().subtract(paid);

        return new InvoiceDTO(
                inv.getId(),
                inv.getOrderId(),
                inv.getTotalAmount(),
                inv.getStatus(),
                inv.getDueDate(),
                inv.getCreatedAt(),
                paid,
                due
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDTO> listByOrder(Long orderId) {
        return invoiceRepository.findByOrderId(orderId).stream()
                .map(inv -> {
                    BigDecimal paid = inv.getPayments().stream()
                            .map(Payment::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal due = inv.getTotalAmount().subtract(paid);
                    return new InvoiceDTO(
                            inv.getId(),
                            inv.getOrderId(),
                            inv.getTotalAmount(),
                            inv.getStatus(),
                            inv.getDueDate(),
                            inv.getCreatedAt(),
                            paid,
                            due
                    );
                })
                .toList();
    }

    @Override
    public PaymentDTO addPayment(Long invoiceId, BigDecimal amount) {
        Invoice inv = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found: " + invoiceId));

        Payment p = new Payment();
        p.setInvoice(inv);
        p.setAmount(amount);
        p.setPaidAt(Instant.now());
        Payment saved = paymentRepository.save(p);

        BigDecimal paid = inv.getPayments().stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (paid.compareTo(inv.getTotalAmount()) >= 0) {
            inv.setStatus(InvoiceStatus.REGLEE);
        } else {
            inv.setStatus(InvoiceStatus.PARTIELLEMENT_REGLEE);
        }
        invoiceRepository.save(inv);

        return new PaymentDTO(
                saved.getId(),
                saved.getInvoice().getId(),
                saved.getAmount(),
                saved.getPaidAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getPayments(Long invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId).stream()
                .map(PaymentMapper::toDto) // ou ton appel statique
                .collect(Collectors.toList());
    }
}
