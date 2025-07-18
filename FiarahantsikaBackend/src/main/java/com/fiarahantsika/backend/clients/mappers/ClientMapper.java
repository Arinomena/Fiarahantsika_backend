package com.fiarahantsika.backend.clients.mappers;

import com.fiarahantsika.backend.clients.dto.ClientDTO;
import com.fiarahantsika.backend.clients.entities.Client;

public final class ClientMapper {

    private ClientMapper() { }

    public static ClientDTO toDto(Client c) {
        return new ClientDTO(
                c.getId(),
                c.getName(),
                c.getAddress(),
                c.getPhone()
        );
    }

    public static Client toEntity(ClientDTO dto) {
        Client c = new Client();
        c.setName(dto.name());
        c.setAddress(dto.address());
        c.setPhone(dto.phone());
        return c;
    }

    public static void updateEntity(ClientDTO dto, Client c) {
        c.setName(dto.name());
        c.setAddress(dto.address());
        c.setPhone(dto.phone());
    }
}

