package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.CreatePackagingEntryRequest;
import com.fiarahantsika.backend.catalog.dto.PackagingEntryDTO;
import java.util.List;

public interface IPackagingEntryService {
    PackagingEntryDTO recordEntry(CreatePackagingEntryRequest req);
    List<PackagingEntryDTO> getEntries(Long packagingId);
}
