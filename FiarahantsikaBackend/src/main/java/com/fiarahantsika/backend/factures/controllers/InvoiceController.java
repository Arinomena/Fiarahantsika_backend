package com.fiarahantsika.backend.factures.controllers;

import com.fiarahantsika.backend.factures.dto.InvoiceDTO;
import com.fiarahantsika.backend.factures.dto.PaymentDTO;
import com.fiarahantsika.backend.factures.services.IInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final IInvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody InvoiceDTO dto) {
        try {
            if (dto == null) return ResponseEntity.badRequest().body(error("Données requises manquantes."));
            InvoiceDTO created = invoiceService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(body(created, "Facture créée."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la création de la facture."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) return ResponseEntity.badRequest().body(error("Identifiant invalide."));
            InvoiceDTO dto = invoiceService.getById(id);
            if (dto == null) return ResponseEntity.status(404).body(error("Facture introuvable."));
            return ResponseEntity.ok(body(dto, "Facture récupérée."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération de la facture."));
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> listByOrder(@PathVariable Long orderId) {
        try {
            if (orderId == null || orderId <= 0) return ResponseEntity.badRequest().body(error("Identifiant de commande invalide."));
            List<InvoiceDTO> data = invoiceService.listByOrder(orderId);
            return ResponseEntity.ok(body(data, "Factures par commande récupérées."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des factures."));
        }
    }

    @GetMapping("/{invoiceId}/payments")
    public ResponseEntity<?> getPayments(@PathVariable Long invoiceId) {
        try {
            if (invoiceId == null || invoiceId <= 0) return ResponseEntity.badRequest().body(error("Identifiant de facture invalide."));
            List<PaymentDTO> data = invoiceService.getPayments(invoiceId);
            return ResponseEntity.ok(body(data, "Paiements de la facture récupérés."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des paiements."));
        }
    }

    @PostMapping("/{invoiceId}/payments")
    public ResponseEntity<?> addPayment(@PathVariable Long invoiceId, @RequestParam BigDecimal amount) {
        try {
            if (invoiceId == null || invoiceId <= 0 || amount == null || amount.signum() <= 0)
                return ResponseEntity.badRequest().body(error("Paramètres de paiement invalides."));
            PaymentDTO created = invoiceService.addPayment(invoiceId, amount);
            return ResponseEntity.status(201).body(body(created, "Paiement enregistré."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(error("Facture introuvable."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de l’enregistrement du paiement."));
        }
    }

    @GetMapping(path = { "", "/" })
    public ResponseEntity<?> listAll() {
        try {
            List<InvoiceDTO> data = invoiceService.listAll();
            return ResponseEntity.ok(body(data, "Liste des factures récupérée."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des factures."));
        }
    }

    private Map<String, Object> body(Object data, String message) {
        Map<String, Object> m = new HashMap<>();
        m.put("message", message);
        m.put("data", data);
        return m;
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> m = new HashMap<>();
        m.put("message", message);
        return m;
    }
}
