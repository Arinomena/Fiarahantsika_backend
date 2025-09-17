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
            if (req == null) {
                return ResponseEntity.badRequest().body(error("Données d’entrée manquantes ou invalides."));
            }
            StockEntryDTO dto = stockEntryService.recordEntry(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(body(dto, "Entrée de stock enregistrée avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de l’enregistrement de l’entrée."));
        }
    }

    @GetMapping
    public ResponseEntity<?> listEntries(@RequestParam(required = false) Long productId) {
        try {
            List<StockEntryDTO> data = stockEntryService.getEntries(productId);
            return ResponseEntity.ok(body(data, "Liste des entrées de stock récupérée avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des entrées."));
        }
    }

    private Map<String, Object> body(Object data, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("data", data);
        return map;
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        return map;
    }
}
