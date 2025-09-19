package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.CreatePackagingExitRequest;
import com.fiarahantsika.backend.catalog.dto.PackagingExitDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPackagingExitService {
    PackagingExitDTO recordExit(CreatePackagingExitRequest req);
    List<PackagingExitDTO> getExits(Long packagingId);
    Page<PackagingExitDTO> getExitsPage(Long packagingId, Pageable pageable);
}
