package com.fiarahantsika.backend.catalog.controllers;

import com.fiarahantsika.backend.catalog.dto.*;
import com.fiarahantsika.backend.catalog.services.IPackagingEntryService;
import com.fiarahantsika.backend.catalog.services.IPackagingExitService;
import com.fiarahantsika.backend.catalog.services.IPackagingService;
import lombok.RequiredArgsConstructor;
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
            List<PackagingDTO> data = service.getAllPackagings();
            return ResponseEntity.ok(body(data, "Liste des packagings récupérée avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des packagings."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(error("Identifiant invalide."));
            }
            PackagingDTO dto = service.getPackagingById(id);
            if (dto == null) {
                return ResponseEntity.status(404).body(error("Packaging introuvable."));
            }
            return ResponseEntity.ok(body(dto, "Packaging récupéré avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération du packaging."));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PackagingDTO dto) {
        try {
            if (dto == null) {
                return ResponseEntity.badRequest().body(error("Données manquantes ou invalides."));
            }
            PackagingDTO created = service.createPackaging(dto);
            return ResponseEntity.status(201).body(body(created, "Packaging créé avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la création du packaging."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PackagingDTO dto) {
        try {
            if (id == null || id <= 0 || dto == null) {
                return ResponseEntity.badRequest().body(error("Paramètres invalides."));
            }
            PackagingDTO updated = service.updatePackaging(id, dto);
            if (updated == null) {
                return ResponseEntity.status(404).body(error("Packaging à mettre à jour introuvable."));
            }
            return ResponseEntity.ok(body(updated, "Packaging mis à jour avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la mise à jour du packaging."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(error("Identifiant invalide."));
            }
            service.deletePackaging(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la suppression du packaging."));
        }
    }

    @GetMapping("/{id}/entries")
    public ResponseEntity<?> listEntries(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(error("Identifiant de packaging invalide."));
            }
            List<PackagingEntryDTO> data = pkgEntryService.getEntries(id);
            return ResponseEntity.ok(body(data, "Historique des entrées de packaging récupéré avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des entrées."));
        }
    }

    @PostMapping("/entries")
    public ResponseEntity<?> createEntry(@RequestBody CreatePackagingEntryRequest req) {
        try {
            if (req == null) {
                return ResponseEntity.badRequest().body(error("Données d’entrée manquantes ou invalides."));
            }
            PackagingEntryDTO created = pkgEntryService.recordEntry(req);
            return ResponseEntity.status(201).body(body(created, "Entrée de packaging enregistrée avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de l’enregistrement de l’entrée."));
        }
    }

    @GetMapping("/{id}/exits")
    public ResponseEntity<?> listExits(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(error("Identifiant de packaging invalide."));
            }
            List<PackagingExitDTO> data = pkgExitService.getExits(id);
            return ResponseEntity.ok(body(data, "Historique des sorties de packaging récupéré avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des sorties."));
        }
    }

    @PostMapping("/exits")
    public ResponseEntity<?> createExitNoPath(@RequestBody CreatePackagingExitRequest req) {
        try {
            if (req == null) {
                return ResponseEntity.badRequest().body(error("Données de sortie manquantes ou invalides."));
            }
            PackagingExitDTO created = pkgExitService.recordExit(req);
            return ResponseEntity.status(201).body(body(created, "Sortie de packaging enregistrée avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de l’enregistrement de la sortie."));
        }
    }

    @PostMapping("/{id}/exits")
    public ResponseEntity<?> createExit(@PathVariable Long id, @RequestBody CreatePackagingExitRequest req) {
        try {
            if (id == null || id <= 0 || req == null) {
                return ResponseEntity.badRequest().body(error("Paramètres invalides pour la sortie."));
            }
            CreatePackagingExitRequest corrected = new CreatePackagingExitRequest(id, req.quantity());
            PackagingExitDTO created = pkgExitService.recordExit(corrected);
            return ResponseEntity.status(201).body(body(created, "Sortie de packaging enregistrée avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de l’enregistrement de la sortie."));
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
