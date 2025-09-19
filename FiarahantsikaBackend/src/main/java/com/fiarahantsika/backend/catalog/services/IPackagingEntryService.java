package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.CreatePackagingEntryRequest;
import com.fiarahantsika.backend.catalog.dto.PackagingEntryDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPackagingEntryService {
    PackagingEntryDTO recordEntry(CreatePackagingEntryRequest req);
    List<PackagingEntryDTO> getEntries(Long packagingId);
    Page<PackagingEntryDTO> getEntriesPage(Long packagingId, Pageable pageable);
}
