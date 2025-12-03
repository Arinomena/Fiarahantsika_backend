package com.fiarahantsika.backend.treasury.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fiarahantsika.backend.treasury.dto.TreasuryTransactionDTO;
import com.fiarahantsika.backend.treasury.services.ITreasuryTransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/treasury")
@RequiredArgsConstructor
public class TreasuryTransactionController {

    private final ITreasuryTransactionService service;

    @GetMapping
    public ResponseEntity<?> list() {
        try {
            List<TreasuryTransactionDTO> transactions = service.getAllTransactions();
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Liste des transactions récupérée avec succès");
            body.put("data", transactions);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur interne du serveur"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            TreasuryTransactionDTO transaction = service.getTransactionById(id);
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Transaction récupérée avec succès");
            body.put("data", transaction);
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Transaction introuvable"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur interne du serveur"));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TreasuryTransactionDTO dto) {
        try {
            TreasuryTransactionDTO created = service.createTransaction(dto);
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Transaction créée avec succès");
            body.put("data", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Données invalides"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur interne du serveur"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TreasuryTransactionDTO dto) {
        try {
            TreasuryTransactionDTO updated = service.updateTransaction(id, dto);
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Transaction mise à jour avec succès");
            body.put("data", updated);
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Données invalides"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur interne du serveur"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deleteTransaction(id);
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Transaction supprimée avec succès");
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Transaction introuvable"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur interne du serveur"));
        }
    }

    @GetMapping("/paged")
    public ResponseEntity<?> listTransactionsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<TreasuryTransactionDTO> result = service.getTransactionsPage(pageable);
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Liste paginée des transactions récupérée avec succès");
            body.put("content", result.getContent());
            body.put("totalPages", result.getTotalPages());
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur interne du serveur"));
        }
    }
}
