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
    public UserDTO register(@RequestBody RegisterRequest r){
        return svc.register(r);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest r){
        return svc.login(r);
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<String> requestReset(@RequestBody PasswordResetRequest r){
        try {
            svc.initiatePasswordReset(r);
            return ResponseEntity.ok("Mail envoyé si l’adresse existe.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Cette adresse n’est pas reconnue.");
        }
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<?> confirmReset(@RequestBody PasswordResetConfirm c){
        svc.confirmPasswordReset(c);
        return ResponseEntity.ok().build();
    }
}
