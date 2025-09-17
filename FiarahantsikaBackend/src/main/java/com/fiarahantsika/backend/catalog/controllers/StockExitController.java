package com.fiarahantsika.backend.catalog.controllers;

import com.fiarahantsika.backend.catalog.dto.CreateStockExitRequest;
import com.fiarahantsika.backend.catalog.dto.StockExitDTO;
import com.fiarahantsika.backend.catalog.services.IStockExitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalog/stock/exits")
@RequiredArgsConstructor
public class StockExitController {

    private final IStockExitService stockExitService;

    @PostMapping
    public ResponseEntity<?> recordExit(@RequestBody CreateStockExitRequest req) {
        try {
            if (req == null) {
                return ResponseEntity.badRequest().body(error("Données de sortie manquantes ou invalides."));
            }
            StockExitDTO dto = stockExitService.recordExit(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(body(dto, "Sortie de stock enregistrée avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de l’enregistrement de la sortie."));
        }
    }

    @GetMapping
    public ResponseEntity<?> listExits(@RequestParam(required = false) Long productId) {
        try {
            List<StockExitDTO> data = stockExitService.getExits(productId);
            return ResponseEntity.ok(body(data, "Liste des sorties de stock récupérée avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des sorties."));
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