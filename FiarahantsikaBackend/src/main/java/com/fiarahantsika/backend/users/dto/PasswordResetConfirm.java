package com.fiarahantsika.backend.users.dto;

public record PasswordResetConfirm(String token, String newPassword) {

}
