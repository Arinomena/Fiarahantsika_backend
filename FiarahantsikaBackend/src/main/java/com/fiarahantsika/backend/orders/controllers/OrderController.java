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

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final IOrderService svc;

    public OrderController(IOrderService svc) {
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(
            @RequestBody CreateOrderRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        OrderDTO dto = svc.createOrder(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> listOrders() {
        return ResponseEntity.ok(svc.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(svc.getOrderById(id));
    }

    /**
     * Met à jour le statut de la commande.
     * Si on passe EN_COURS_DE_LIVRAISON depuis ENREGISTREE,
     * on déclenche les sorties de stock et packaging.
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status
    ) {
        OrderDTO dto = svc.updateStatus(id, status);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(
            @PathVariable Long id,
            @RequestBody CreateOrderRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        OrderDTO dto = svc.updateOrder(id, request, userDetails.getUsername());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        svc.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
