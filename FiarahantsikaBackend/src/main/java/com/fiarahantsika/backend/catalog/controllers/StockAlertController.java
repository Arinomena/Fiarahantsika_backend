package com.fiarahantsika.backend.catalog.controllers;

import com.fiarahantsika.backend.catalog.dto.StockAlertDTO;
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
    public List<StockAlertDTO> listAlerts() {
        return alertService.getAlerts();
    }

    @PostMapping("/{productId}/resolve")
    public ResponseEntity<Void> resolve(
            @PathVariable Long productId
    ) {
        alertService.resolveAlert(productId);
        return ResponseEntity.noContent().build();
    }
}