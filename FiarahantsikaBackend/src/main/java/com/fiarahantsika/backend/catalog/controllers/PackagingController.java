package com.fiarahantsika.backend.catalog.controllers;

import com.fiarahantsika.backend.catalog.dto.*;
import com.fiarahantsika.backend.catalog.services.IPackagingEntryService;
import com.fiarahantsika.backend.catalog.services.IPackagingExitService;
import com.fiarahantsika.backend.catalog.services.IPackagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalog/packagings")
@RequiredArgsConstructor
public class PackagingController {

    private final IPackagingService service;
    private final IPackagingEntryService pkgEntryService;
    private final IPackagingExitService pkgExitService;

    @GetMapping
    public ResponseEntity<?> list() {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Liste des packagings récupérée avec succès");
            body.put("data", service.getAllPackagings());
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            PackagingDTO dto = service.getPackagingById(id);
            if (dto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Packaging introuvable");
            }
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Packaging récupéré avec succès");
            body.put("data", dto);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PackagingDTO dto) {
        try {
            PackagingDTO created = service.createPackaging(dto);
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Packaging créé avec succès");
            body.put("data", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Données invalides");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PackagingDTO dto) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Packaging mis à jour avec succès");
            body.put("data", service.updatePackaging(id, dto));
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Données invalides");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deletePackaging(id);
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Packaging supprimé avec succès");
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @GetMapping("/{id}/entries")
    public ResponseEntity<?> listEntries(@PathVariable Long id) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Liste des entrées récupérée avec succès");
            body.put("data", pkgEntryService.getEntries(id));
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @PostMapping("/entries")
    public ResponseEntity<?> createEntry(@RequestBody CreatePackagingEntryRequest req) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Entrée de packaging enregistrée avec succès");
            body.put("data", pkgEntryService.recordEntry(req));
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Données invalides");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @GetMapping("/{id}/exits")
    public ResponseEntity<?> listExits(@PathVariable Long id) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Liste des sorties récupérée avec succès");
            body.put("data", pkgExitService.getExits(id));
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @PostMapping("/exits")
    public ResponseEntity<?> createExitNoPath(@RequestBody CreatePackagingExitRequest req) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Sortie de packaging enregistrée avec succès");
            body.put("data", pkgExitService.recordExit(req));
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Données invalides");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @PostMapping("/{id}/exits")
    public ResponseEntity<?> createExit(@PathVariable Long id, @RequestBody CreatePackagingExitRequest req) {
        try {
            CreatePackagingExitRequest corrected = new CreatePackagingExitRequest(id, req.quantity());
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Sortie de packaging enregistrée avec succès");
            body.put("data", pkgExitService.recordExit(corrected));
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Données invalides");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }
}