package com.fiarahantsika.backend.catalog.controllers;

import com.fiarahantsika.backend.catalog.dto.StockAlertDTO;
import com.fiarahantsika.backend.catalog.services.IStockAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalog/stock/alerts")
@RequiredArgsConstructor
public class StockAlertController {

    private final IStockAlertService alertService;

    @GetMapping
    public ResponseEntity<?> listAlerts(@RequestParam(name = "resolved", required = false) Boolean resolved) {
        try {
            List<StockAlertDTO> data = alertService.getAlerts(resolved);
            return ResponseEntity.ok(body(data, "Liste des alertes de stock récupérée avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des alertes."));
        }
    }

    @PostMapping("/{alertId}/resolve")
    public ResponseEntity<?> resolve(@PathVariable Long alertId) {
        try {
            if (alertId == null || alertId <= 0) {
                return ResponseEntity.badRequest().body(error("Identifiant d’alerte invalide."));
            }
            alertService.setResolved(alertId, true);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(error("Alerte introuvable."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la résolution de l’alerte."));
        }
    }

    @PostMapping("/{alertId}/unresolve")
    public ResponseEntity<?> unresolve(@PathVariable Long alertId) {
        try {
            if (alertId == null || alertId <= 0) {
                return ResponseEntity.badRequest().body(error("Identifiant d’alerte invalide."));
            }
            alertService.setResolved(alertId, false);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(error("Alerte introuvable."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de l’annulation de la résolution."));
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
