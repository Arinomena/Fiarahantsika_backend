package com.fiarahantsika.backend.users.dto;

public record PasswordResetConfirm(
        String email,
        String token,
        String newPassword
) {}
