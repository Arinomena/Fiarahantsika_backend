package com.fiarahantsika.backend.factures.controllers;

import com.fiarahantsika.backend.factures.dto.InvoiceDTO;
import com.fiarahantsika.backend.factures.dto.PaymentDTO;
import com.fiarahantsika.backend.factures.services.IInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final IInvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<InvoiceDTO> create(@RequestBody InvoiceDTO dto) {
        InvoiceDTO created = invoiceService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public InvoiceDTO get(@PathVariable Long id) {
        return invoiceService.getById(id);
    }

    @GetMapping("/order/{orderId}")
    public List<InvoiceDTO> listByOrder(@PathVariable Long orderId) {
        return invoiceService.listByOrder(orderId);
    }

    @GetMapping("/{invoiceId}/payments")
    public List<PaymentDTO> getPayments(@PathVariable Long invoiceId) {
        return invoiceService.getPayments(invoiceId);
    }

    @PostMapping("/{invoiceId}/payments")
    public PaymentDTO addPayment(
            @PathVariable Long invoiceId,
            @RequestParam BigDecimal amount) {
        return invoiceService.addPayment(invoiceId, amount);
    }

    @GetMapping(path = { "", "/" })
    public List<InvoiceDTO> listAll() {
        return invoiceService.listAll();
    }


}