package com.fiarahantsika.backend.catalog.controllers;

import com.fiarahantsika.backend.catalog.dto.StockAlertDTO;
import com.fiarahantsika.backend.catalog.entities.Product;
import com.fiarahantsika.backend.catalog.entities.StockAlert;
import com.fiarahantsika.backend.catalog.services.IStockAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/catalog/stock/alerts")
@RequiredArgsConstructor
public class StockAlertController {

    private final IStockAlertService alertService;

    @GetMapping
    public List<StockAlertDTO> listAlerts(
            @RequestParam(name = "resolved", required = false) Boolean resolved
    ) {
        return alertService.getAlerts(resolved);
    }

    @PostMapping("/{alertId}/resolve")
    public ResponseEntity<Void> resolve(@PathVariable Long alertId) {
        alertService.setResolved(alertId, true);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{alertId}/unresolve")
    public ResponseEntity<Void> unresolve(@PathVariable Long alertId) {
        alertService.setResolved(alertId, false);
        return ResponseEntity.noContent().build();
    }

}