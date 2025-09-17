package com.fiarahantsika.backend.stats.controller;

import com.fiarahantsika.backend.stats.dto.*;
import com.fiarahantsika.backend.stats.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/sales-by-day")
    public ResponseEntity<?> salesByDay() {
        try {
            List<SalesByDayDTO> data = statsService.getSalesByDay();
            return ResponseEntity.ok(body(data, "Ventes par jour récupérées."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors du calcul des ventes par jour."));
        }
    }

    @GetMapping("/top-products")
    public ResponseEntity<?> topProducts() {
        try {
            List<TopProductDTO> data = statsService.getTopProducts();
            return ResponseEntity.ok(body(data, "Top produits récupéré."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération du top produits."));
        }
    }

    @GetMapping("/invoice-status")
    public ResponseEntity<?> invoiceStatus() {
        try {
            List<InvoiceStatusDTO> data = statsService.getInvoiceStatusStats();
            return ResponseEntity.ok(body(data, "Statuts de factures récupérés."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des statuts de factures."));
        }
    }

    @GetMapping("/average-basket")
    public ResponseEntity<?> averageBasket() {
        try {
            AverageBasketDTO data = statsService.getAverageBasket();
            return ResponseEntity.ok(body(data, "Panier moyen récupéré."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors du calcul du panier moyen."));
        }
    }

    @GetMapping("/sales-by-client")
    public ResponseEntity<?> salesByClient() {
        try {
            List<SalesByClientDTO> data = statsService.getSalesByClient();
            return ResponseEntity.ok(body(data, "Ventes par client récupérées."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des ventes par client."));
        }
    }

    @GetMapping("/sales-by-seller/current-month")
    public ResponseEntity<?> salesBySellerCurrentMonth() {
        try {
            List<SalesBySellerDTO> data = statsService.getSalesBySellerCurrentMonth();
            return ResponseEntity.ok(body(data, "Ventes par vendeur (mois courant) récupérées."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des ventes par vendeur."));
        }
    }

    @GetMapping("/sales-by-seller")
    public ResponseEntity<?> salesBySellerBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        try {
            List<SalesBySellerDTO> data = statsService.getSalesBySellerBetween(start, end);
            return ResponseEntity.ok(body(data, "Ventes par vendeur récupérées pour la période."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des ventes par vendeur."));
        }
    }

    private Map<String, Object> body(Object data, String message) {
        Map<String, Object> m = new HashMap<>();
        m.put("message", message);
        m.put("data", data);
        return m;
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> m = new HashMap<>();
        m.put("message", message);
        return m;
    }
}
