package com.fiarahantsika.backend.catalog.controllers;

import com.fiarahantsika.backend.catalog.dto.ProductDTO;
import com.fiarahantsika.backend.catalog.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalog/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService service;

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(required = false) String search) {
        try {
            List<ProductDTO> data = service.getProducts(search);
            return ResponseEntity.ok(body(data, "Liste des produits récupérée avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des produits."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(error("Identifiant invalide."));
            }
            ProductDTO dto = service.getProductById(id);
            if (dto == null) {
                return ResponseEntity.status(404).body(error("Produit introuvable."));
            }
            return ResponseEntity.ok(body(dto, "Produit récupéré avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération du produit."));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductDTO dto) {
        try {
            if (dto == null) {
                return ResponseEntity.badRequest().body(error("Données manquantes ou invalides."));
            }
            ProductDTO created = service.createProduct(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(body(created, "Produit créé avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la création du produit."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        try {
            if (id == null || id <= 0 || dto == null) {
                return ResponseEntity.badRequest().body(error("Paramètres invalides."));
            }
            ProductDTO updated = service.updateProduct(id, dto);
            if (updated == null) {
                return ResponseEntity.status(404).body(error("Produit à mettre à jour introuvable."));
            }
            return ResponseEntity.ok(body(updated, "Produit mis à jour avec succès."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la mise à jour du produit."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(error("Identifiant invalide."));
            }
            service.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la suppression du produit."));
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
