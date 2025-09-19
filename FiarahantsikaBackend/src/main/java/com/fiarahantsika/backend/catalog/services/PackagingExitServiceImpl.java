package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.CreatePackagingExitRequest;
import com.fiarahantsika.backend.catalog.dto.PackagingExitDTO;
import com.fiarahantsika.backend.catalog.entities.Packaging;
import com.fiarahantsika.backend.catalog.entities.PackagingExit;
import com.fiarahantsika.backend.catalog.repositories.PackagingExitRepository;
import com.fiarahantsika.backend.catalog.repositories.PackagingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PackagingExitServiceImpl implements IPackagingExitService {

    private final PackagingExitRepository exitRepo;
    private final PackagingRepository     packagingRepo;

    @Override
    public PackagingExitDTO recordExit(CreatePackagingExitRequest req) {
        Packaging pkg = packagingRepo.findById(req.packagingId())
                .orElseThrow(() -> new IllegalArgumentException("Packaging introuvable : " + req.packagingId()));

        int qty = req.quantity();
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantité doit être > 0");
        }
        if (pkg.getCurrentStock() < qty) {
            throw new IllegalArgumentException(
                    "Stock insuffisant pour le packaging " + pkg.getId() +
                            " (disponible=" + pkg.getCurrentStock() + ", demandé=" + qty + ")"
            );
        }

        pkg.setCurrentStock(pkg.getCurrentStock() - qty);
        packagingRepo.save(pkg);

        PackagingExit ex = new PackagingExit();
        ex.setPackaging(pkg);
        ex.setQuantity(qty);
        ex.setExitDate(Instant.now());
        ex.setRemainingStock(pkg.getCurrentStock());
        PackagingExit saved = exitRepo.save(ex);

        return toDto(saved, pkg.getCurrentStock());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PackagingExitDTO> getExits(Long packagingId) {
        List<PackagingExit> list = (packagingId != null)
                ? exitRepo.findByPackagingId(packagingId)
                : exitRepo.findAll();
        return list.stream()
                .map(e -> toDto(e, e.getPackaging().getCurrentStock()))
                .toList();
    }

    private static PackagingExitDTO toDto(PackagingExit e, Integer remainingStock) {
        return new PackagingExitDTO(
                e.getId(),
                e.getPackaging().getId(),
                e.getQuantity(),
                e.getExitDate(),
                e.getRemainingStock()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PackagingExitDTO> getExitsPage(Long packagingId, Pageable pageable) {
        return (packagingId != null)
                ? exitRepo.findByPackagingId(packagingId, pageable)
                .map(e -> toDto(e, e.getPackaging().getCurrentStock()))
                : exitRepo.findAll(pageable)
                .map(e -> toDto(e, e.getPackaging().getCurrentStock()));
    }
}
