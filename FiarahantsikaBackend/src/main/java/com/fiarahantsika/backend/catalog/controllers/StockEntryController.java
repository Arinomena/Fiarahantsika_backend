package com.fiarahantsika.backend.catalog.controllers;

import com.fiarahantsika.backend.catalog.dto.CreateStockEntryRequest;
import com.fiarahantsika.backend.catalog.dto.StockEntryDTO;
import com.fiarahantsika.backend.catalog.services.IStockEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalog/stock/entries")
@RequiredArgsConstructor
public class StockEntryController {

    private final IStockEntryService stockEntryService;

    @PostMapping
    public ResponseEntity<?> recordEntry(@RequestBody CreateStockEntryRequest req) {
        try {
            StockEntryDTO dto = stockEntryService.recordEntry(req);
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Entrée de stock enregistrée avec succès");
            body.put("data", dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Données invalides");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @GetMapping
    public ResponseEntity<?> listEntries(@RequestParam(required = false) Long productId) {
        try {
            List<StockEntryDTO> entries = stockEntryService.getEntries(productId);
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Liste des entrées récupérée avec succès");
            body.put("data", entries);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }
}
