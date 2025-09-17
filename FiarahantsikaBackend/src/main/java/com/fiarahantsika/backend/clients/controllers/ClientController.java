package com.fiarahantsika.backend.clients.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fiarahantsika.backend.clients.dto.ClientDTO;
import com.fiarahantsika.backend.clients.services.IClientService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    private final IClientService service;

    @GetMapping
    public ResponseEntity<?> list() {
        try {
            List<ClientDTO> data = service.getAllClients();
            return ResponseEntity.ok(body(data, "Liste des clients récupérée."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des clients."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) return ResponseEntity.badRequest().body(error("Identifiant invalide."));
            ClientDTO dto = service.getClientById(id);
            if (dto == null) return ResponseEntity.status(404).body(error("Client introuvable."));
            return ResponseEntity.ok(body(dto, "Client récupéré."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération du client."));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ClientDTO dto) {
        try {
            if (dto == null) return ResponseEntity.badRequest().body(error("Données requises manquantes."));
            ClientDTO created = service.createClient(dto);
            return ResponseEntity.status(201).body(body(created, "Client créé."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la création du client."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ClientDTO dto) {
        try {
            if (id == null || id <= 0 || dto == null) return ResponseEntity.badRequest().body(error("Paramètres invalides."));
            ClientDTO updated = service.updateClient(id, dto);
            if (updated == null) return ResponseEntity.status(404).body(error("Client à mettre à jour introuvable."));
            return ResponseEntity.ok(body(updated, "Client mis à jour."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la mise à jour du client."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) return ResponseEntity.badRequest().body(error("Identifiant invalide."));
            service.deleteClient(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(error("Client introuvable."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la suppression du client."));
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
