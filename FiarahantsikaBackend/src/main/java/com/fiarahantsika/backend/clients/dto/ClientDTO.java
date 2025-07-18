package com.fiarahantsika.backend.clients.dto;

public record ClientDTO(
        Long id,
        String name,
        String address,
        String phone
) {}
