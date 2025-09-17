package com.fiarahantsika.backend.catalog.controllers;

import com.fiarahantsika.backend.catalog.dto.StockAlertDTO;
import com.fiarahantsika.backend.catalog.services.IStockAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/catalog/stock/alerts")
@RequiredArgsConstructor
public class StockAlertController {

    private final IStockAlertService alertService;

    @GetMapping
    public ResponseEntity<?> listAlerts(@RequestParam(name = "resolved", required = false) Boolean resolved) {
        try {
            return ResponseEntity.ok(alertService.getAlerts(resolved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @PostMapping("/{alertId}/resolve")
    public ResponseEntity<?> resolve(@PathVariable Long alertId) {
        try {
            alertService.setResolved(alertId, true);
            return ResponseEntity.ok("Alerte résolue");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @PostMapping("/{alertId}/unresolve")
    public ResponseEntity<?> unresolve(@PathVariable Long alertId) {
        try {
            alertService.setResolved(alertId, false);
            return ResponseEntity.ok("Alerte non résolue");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }
}
