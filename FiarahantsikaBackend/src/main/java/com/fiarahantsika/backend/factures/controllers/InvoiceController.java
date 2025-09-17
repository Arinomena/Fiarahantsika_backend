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
    public ResponseEntity<?> create(@RequestBody InvoiceDTO dto) {
        try {
            InvoiceDTO created = invoiceService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Données invalides");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            InvoiceDTO dto = invoiceService.getById(id);
            if (dto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Facture introuvable");
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> listByOrder(@PathVariable Long orderId) {
        try {
            return ResponseEntity.ok(invoiceService.listByOrder(orderId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @GetMapping("/{invoiceId}/payments")
    public ResponseEntity<?> getPayments(@PathVariable Long invoiceId) {
        try {
            return ResponseEntity.ok(invoiceService.getPayments(invoiceId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @PostMapping("/{invoiceId}/payments")
    public ResponseEntity<?> addPayment(
            @PathVariable Long invoiceId,
            @RequestParam BigDecimal amount) {
        try {
            return ResponseEntity.ok(invoiceService.addPayment(invoiceId, amount));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Montant invalide");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @GetMapping(path = { "", "/" })
    public ResponseEntity<?> listAll() {
        try {
            return ResponseEntity.ok(invoiceService.listAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }
}
