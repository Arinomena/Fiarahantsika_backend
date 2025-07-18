package com.fiarahantsika.backend.clients.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fiarahantsika.backend.clients.dto.ClientDTO;
import com.fiarahantsika.backend.clients.services.IClientService;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    private final IClientService service;

    @GetMapping
    public List<ClientDTO> list() {
        return service.getAllClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getClientById(id));
    }

    @PostMapping
    public ResponseEntity<ClientDTO> create(@RequestBody ClientDTO dto) {
        ClientDTO created = service.createClient(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> update(
            @PathVariable Long id,
            @RequestBody ClientDTO dto
    ) {
        return ResponseEntity.ok(service.updateClient(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
