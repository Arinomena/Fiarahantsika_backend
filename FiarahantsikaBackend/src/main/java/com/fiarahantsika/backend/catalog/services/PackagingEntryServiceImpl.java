package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.CreatePackagingEntryRequest;
import com.fiarahantsika.backend.catalog.dto.PackagingEntryDTO;
import com.fiarahantsika.backend.catalog.entities.Packaging;
import com.fiarahantsika.backend.catalog.entities.PackagingEntry;
import com.fiarahantsika.backend.catalog.repositories.PackagingEntryRepository;
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
public class PackagingEntryServiceImpl implements IPackagingEntryService {

    private final PackagingEntryRepository entryRepo;
    private final PackagingRepository      packagingRepo;

    @Override
    public PackagingEntryDTO recordEntry(CreatePackagingEntryRequest req) {
        Packaging pkg = packagingRepo.findById(req.packagingId())
                .orElseThrow(() -> new IllegalArgumentException("Packaging introuvable: " + req.packagingId()));
        int qty = req.quantity();
        if (qty <= 0) throw new IllegalArgumentException("Quantité doit être > 0");

        pkg.setCurrentStock(pkg.getCurrentStock() + qty);
        packagingRepo.save(pkg);

        PackagingEntry ent = new PackagingEntry();
        ent.setPackaging(pkg);
        ent.setQuantity(qty);
        ent.setEntryDate(Instant.now());
        ent.setRemainingStock(pkg.getCurrentStock());
        PackagingEntry saved = entryRepo.save(ent);

        return new PackagingEntryDTO(
                saved.getId(),
                pkg.getId(),
                qty,
                saved.getEntryDate(),
                saved.getRemainingStock()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PackagingEntryDTO> getEntries(Long packagingId) {
        List<PackagingEntry> list = packagingId != null
                ? entryRepo.findByPackagingId(packagingId)
                : entryRepo.findAll();
        return list.stream()
                .map(e -> new PackagingEntryDTO(
                        e.getId(),
                        e.getPackaging().getId(),
                        e.getQuantity(),
                        e.getEntryDate(),
                        e.getRemainingStock()
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PackagingEntryDTO> getEntriesPage(Long packagingId, Pageable pageable) {
        return (packagingId != null)
                ? entryRepo.findByPackagingId(packagingId, pageable)
                .map(e -> new PackagingEntryDTO(
                        e.getId(),
                        e.getPackaging().getId(),
                        e.getQuantity(),
                        e.getEntryDate(),
                        e.getRemainingStock()
                ))
                : entryRepo.findAll(pageable)
                .map(e -> new PackagingEntryDTO(
                        e.getId(),
                        e.getPackaging().getId(),
                        e.getQuantity(),
                        e.getEntryDate(),
                        e.getRemainingStock()
                ));
    }
}
