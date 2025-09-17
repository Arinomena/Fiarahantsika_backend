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
            StockExitDTO dto = stockExitService.recordExit(req);
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Sortie de stock enregistrée avec succès");
            body.put("data", dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Données invalides");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @GetMapping
    public ResponseEntity<?> listExits(@RequestParam(required = false) Long productId) {
        try {
            List<StockExitDTO> exits = stockExitService.getExits(productId);
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Liste des sorties récupérée avec succès");
            body.put("data", exits);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }
}
