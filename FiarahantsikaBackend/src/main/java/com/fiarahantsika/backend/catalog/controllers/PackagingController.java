package com.fiarahantsika.backend.catalog.controllers;

import com.fiarahantsika.backend.catalog.dto.*;
import com.fiarahantsika.backend.catalog.services.IPackagingEntryService;
import com.fiarahantsika.backend.catalog.services.IPackagingExitService;
import com.fiarahantsika.backend.catalog.services.IPackagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/packagings")
@RequiredArgsConstructor
public class PackagingController {

    private final IPackagingService        service;
    private final IPackagingEntryService   pkgEntryService;
    private final IPackagingExitService   pkgExitService;

    @GetMapping
    public List<PackagingDTO> list() {
        return service.getAllPackagings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackagingDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPackagingById(id));
    }

    @PostMapping
    public ResponseEntity<PackagingDTO> create(@RequestBody PackagingDTO dto) {
        PackagingDTO created = service.createPackaging(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackagingDTO> update(
            @PathVariable Long id,
            @RequestBody PackagingDTO dto
    ) {
        return ResponseEntity.ok(service.updatePackaging(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletePackaging(id);
        return ResponseEntity.noContent().build();
    }

    // --- ENTRÉES DE PACKAGING ---
    @GetMapping("/{id}/entries")
    public List<PackagingEntryDTO> listEntries(@PathVariable Long id) {
        return pkgEntryService.getEntries(id);
    }

    @PostMapping("/entries")
    public PackagingEntryDTO createEntry(
            @RequestBody CreatePackagingEntryRequest req
    ) {
        return pkgEntryService.recordEntry(req);
    }

    // on liste les exits
    @GetMapping("/{id}/exits")
    public List<PackagingExitDTO> listExits(@PathVariable Long id) {
        return pkgExitService.getExits(id);
    }

    @PostMapping("/exits")
    public PackagingExitDTO createExitNoPath(@RequestBody CreatePackagingExitRequest req) {
        return pkgExitService.recordExit(req);
    }

    // on crée une exit
    @PostMapping("/{id}/exits")
    public PackagingExitDTO createExit(
            @PathVariable Long id,
            @RequestBody CreatePackagingExitRequest req
    ) {
        CreatePackagingExitRequest corrected =
                new CreatePackagingExitRequest(id, req.quantity());
        return pkgExitService.recordExit(corrected);
    }
}
