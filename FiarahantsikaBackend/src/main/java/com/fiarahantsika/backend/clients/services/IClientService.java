package com.fiarahantsika.backend.clients.services;

import com.fiarahantsika.backend.clients.dto.ClientDTO;
import java.util.List;

public interface IClientService {
    List<ClientDTO> getAllClients();
    ClientDTO getClientById(Long id);
    ClientDTO createClient(ClientDTO dto);
    ClientDTO updateClient(Long id, ClientDTO dto);
    void deleteClient(Long id);
}
