package com.fiarahantsika.backend.users.controllers;

import com.fiarahantsika.backend.common.exception.ResourceNotFoundException;
import com.fiarahantsika.backend.users.dto.*;
import com.fiarahantsika.backend.users.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final IUserService svc;

    public AuthController(IUserService s){
        this.svc = s;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest r){
        try {
            if (r == null) return ResponseEntity.badRequest().body(error("Données d’inscription manquantes."));
            UserDTO dto = svc.register(r);
            return ResponseEntity.status(201).body(body(dto, "Inscription réussie."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(error("Données d’inscription invalides."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de l’inscription."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest r){
        try {
            if (r == null) return ResponseEntity.badRequest().body(error("Identifiants manquants."));
            AuthResponse auth = svc.login(r);
            return ResponseEntity.ok(body(auth, "Connexion réussie."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(400).body(error("Identifiants invalides."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la connexion."));
        }
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<?> requestReset(@RequestBody PasswordResetRequest r){
        try {
            if (r == null) return ResponseEntity.badRequest().body(error("Données manquantes."));
            svc.initiatePasswordReset(r);
            return ResponseEntity.ok(body(null, "Mail envoyé si l’adresse existe."));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error("Cette adresse n’est pas reconnue."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la demande de réinitialisation."));
        }
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<?> confirmReset(@RequestBody PasswordResetConfirm c){
        try {
            if (c == null) return ResponseEntity.badRequest().body(error("Données manquantes."));
            svc.confirmPasswordReset(c);
            return ResponseEntity.ok(body(null, "Mot de passe réinitialisé."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(error("Jeton ou données invalides."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Erreur interne lors de la réinitialisation du mot de passe."));
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
