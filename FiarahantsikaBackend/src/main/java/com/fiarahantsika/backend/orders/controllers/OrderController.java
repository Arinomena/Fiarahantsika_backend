package com.fiarahantsika.backend.orders.controllers;

import com.fiarahantsika.backend.common.enums.OrderStatus;
import com.fiarahantsika.backend.orders.dto.CreateOrderRequest;
import com.fiarahantsika.backend.orders.dto.OrderDTO;
import com.fiarahantsika.backend.orders.services.IOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final IOrderService svc;

    public OrderController(IOrderService svc) {
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails == null) return ResponseEntity.status(401).body(error("Session expirée. Veuillez vous reconnecter."));
            if (request == null) return ResponseEntity.badRequest().body(error("Données de commande manquantes."));
            OrderDTO dto = svc.createOrder(request, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(body(dto, "Commande créée."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(error("Paramètres de commande invalides."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la création de la commande."));
        }
    }

    @GetMapping
    public ResponseEntity<?> listOrders() {
        try {
            List<OrderDTO> data = svc.getAllOrders();
            return ResponseEntity.ok(body(data, "Liste des commandes récupérée."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des commandes."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) return ResponseEntity.badRequest().body(error("Identifiant de commande invalide."));
            OrderDTO dto = svc.getOrderById(id);
            if (dto == null) return ResponseEntity.status(404).body(error("Commande introuvable."));
            return ResponseEntity.ok(body(dto, "Commande récupérée."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération de la commande."));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        try {
            if (id == null || id <= 0 || status == null) return ResponseEntity.badRequest().body(error("Paramètres invalides."));
            OrderDTO dto = svc.updateStatus(id, status);
            if (dto == null) return ResponseEntity.status(404).body(error("Commande introuvable."));
            return ResponseEntity.ok(body(dto, "Statut de la commande mis à jour."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(error("Statut de commande invalide."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la mise à jour du statut."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody CreateOrderRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails == null) return ResponseEntity.status(401).body(error("Session expirée. Veuillez vous reconnecter."));
            if (id == null || id <= 0 || request == null) return ResponseEntity.badRequest().body(error("Paramètres invalides."));
            OrderDTO dto = svc.updateOrder(id, request, userDetails.getUsername());
            if (dto == null) return ResponseEntity.status(404).body(error("Commande à mettre à jour introuvable."));
            return ResponseEntity.ok(body(dto, "Commande mise à jour."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la mise à jour de la commande."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) return ResponseEntity.badRequest().body(error("Identifiant invalide."));
            svc.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(error("Commande introuvable."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la suppression de la commande."));
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
