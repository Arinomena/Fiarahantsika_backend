package com.fiarahantsika.backend.users.controllers;

import com.fiarahantsika.backend.users.dto.UpdateUserRequest;
import com.fiarahantsika.backend.users.dto.UserDTO;
import com.fiarahantsika.backend.users.services.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService svc;

    public UserController(IUserService svc) {
        this.svc = svc;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<UserDTO> users = svc.getAllUsers();
            return ResponseEntity.ok(body(users, "Liste des utilisateurs récupérée."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération des utilisateurs."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) return ResponseEntity.badRequest().body(error("Identifiant invalide."));
            UserDTO user = svc.getUserById(id);
            if (user == null) return ResponseEntity.status(404).body(error("Utilisateur introuvable."));
            return ResponseEntity.ok(body(user, "Utilisateur récupéré."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la récupération de l’utilisateur."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateUserRequest dto) {
        try {
            if (id == null || id <= 0 || dto == null) return ResponseEntity.badRequest().body(error("Paramètres invalides."));
            UserDTO updated = svc.updateUser(id, dto);
            if (updated == null) return ResponseEntity.status(404).body(error("Utilisateur à mettre à jour introuvable."));
            return ResponseEntity.ok(body(updated, "Utilisateur mis à jour."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la mise à jour de l’utilisateur."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) return ResponseEntity.badRequest().body(error("Identifiant invalide."));
            svc.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(error("Utilisateur introuvable."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la suppression de l’utilisateur."));
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