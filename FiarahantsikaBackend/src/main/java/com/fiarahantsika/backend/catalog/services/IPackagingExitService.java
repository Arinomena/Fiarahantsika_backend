package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.CreatePackagingExitRequest;
import com.fiarahantsika.backend.catalog.dto.PackagingExitDTO;
import java.util.List;

public interface IPackagingExitService {
    PackagingExitDTO recordExit(CreatePackagingExitRequest req);
    List<PackagingExitDTO> getExits(Long packagingId);
}
