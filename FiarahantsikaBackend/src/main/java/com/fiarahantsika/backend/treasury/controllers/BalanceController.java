package com.fiarahantsika.backend.treasury.controllers;

import com.fiarahantsika.backend.treasury.dto.BalanceDTO;
import com.fiarahantsika.backend.treasury.services.IBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final IBalanceService service;

    @GetMapping
    public ResponseEntity<?> list() {
        List<BalanceDTO> balances = service.getAllBalances();
        return ResponseEntity.ok(Map.of("message", "Balances récupérées avec succès", "data", balances));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            BalanceDTO balance = service.getBalanceById(id);
            return ResponseEntity.ok(Map.of("message", "Balance récupérée avec succès", "data", balance));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> getByType(@PathVariable String type) {
        try {
            BalanceDTO balance = service.getBalanceByType(type);
            return ResponseEntity.ok(Map.of("message", "Balance récupérée avec succès", "data", balance));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrUpdate(@RequestBody BalanceDTO dto) {
        BalanceDTO saved = service.createOrUpdateBalance(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Balance enregistrée", "data", saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteBalance(id);
        return ResponseEntity.ok(Map.of("message", "Balance supprimée avec succès"));
    }

    @GetMapping("/global")
    public ResponseEntity<?> global() {
        Double global = service.getGlobalBalance();
        return ResponseEntity.ok(Map.of("message", "Balance globale calculée", "data", global));
    }
}
