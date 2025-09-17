package com.fiarahantsika.backend.users.controllers;

import com.fiarahantsika.backend.common.exception.ResourceNotFoundException;
import com.fiarahantsika.backend.users.dto.*;
import com.fiarahantsika.backend.users.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            UserDTO dto = svc.register(r);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Données invalides");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest r){
        try {
            AuthResponse auth = svc.login(r);
            return ResponseEntity.ok(auth);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identifiants invalides");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentification requise");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<?> requestReset(@RequestBody PasswordResetRequest r){
        try {
            svc.initiatePasswordReset(r);
            return ResponseEntity.ok("Mail envoyé si l’adresse existe");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adresse non reconnue");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<?> confirmReset(@RequestBody PasswordResetConfirm c){
        try {
            svc.confirmPasswordReset(c);
            return ResponseEntity.ok("Mot de passe réinitialisé");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Données invalides");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
        }
    }
}
