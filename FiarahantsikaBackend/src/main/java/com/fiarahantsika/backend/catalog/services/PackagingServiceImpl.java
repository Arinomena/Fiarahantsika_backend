// src/main/java/com/fiarahantsika/backend/catalog/services/PackagingServiceImpl.java
package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.PackagingDTO;
import com.fiarahantsika.backend.catalog.entities.Packaging;
import com.fiarahantsika.backend.common.enums.PackagingFormat;
import com.fiarahantsika.backend.catalog.repositories.PackagingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PackagingServiceImpl implements IPackagingService {
    private final PackagingRepository repo;

    @Override
    public List<PackagingDTO> getAllPackagings() {
        return repo.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public PackagingDTO getPackagingById(Long id) {
        Packaging pk = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Packaging non trouvé " + id));
        return toDto(pk);
    }

    @Override
    public PackagingDTO createPackaging(PackagingDTO dto) {
        Packaging pk = new Packaging();
        fromDto(dto, pk);
        return toDto(repo.save(pk));
    }

    @Override
    public PackagingDTO updatePackaging(Long id, PackagingDTO dto) {
        Packaging pk = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Packaging non trouvé " + id));
        fromDto(dto, pk);
        return toDto(repo.save(pk));
    }

    @Override
    public void deletePackaging(Long id) {
        repo.deleteById(id);
    }

    private PackagingDTO toDto(Packaging pk) {
       return new PackagingDTO(
                pk.getId(),
                pk.getFormat(),        // PackagingFormat
                pk.getType(),          // String
                pk.getPrice(),
                pk.getCurrentStock()
        );
    }

    private void fromDto(PackagingDTO dto, Packaging pk) {
        pk.setFormat(dto.format());
        pk.setType(dto.type());
        pk.setPrice(dto.price());
        pk.setCurrentStock(dto.currentStock());
    }
}
