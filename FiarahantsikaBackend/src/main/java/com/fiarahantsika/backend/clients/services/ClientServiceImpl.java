package com.fiarahantsika.backend.clients.services;

import com.fiarahantsika.backend.clients.dto.ClientDTO;
import com.fiarahantsika.backend.clients.entities.Client;
import com.fiarahantsika.backend.clients.mappers.ClientMapper;
import com.fiarahantsika.backend.clients.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements IClientService {

    private final ClientRepository repo;

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> getAllClients() {
        return repo.findAll().stream()
                .map(ClientMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO getClientById(Long id) {
        Client c = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé : " + id));
        return ClientMapper.toDto(c);
    }

    @Override
    public ClientDTO createClient(ClientDTO dto) {
        Client c = ClientMapper.toEntity(dto);
        Client saved = repo.save(c);
        return ClientMapper.toDto(saved);
    }

    @Override
    public ClientDTO updateClient(Long id, ClientDTO dto) {
        Client c = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé : " + id));
        ClientMapper.updateEntity(dto, c);
        Client updated = repo.save(c);
        return ClientMapper.toDto(updated);
    }

    @Override
    public void deleteClient(Long id) {
        repo.deleteById(id);
    }
}
