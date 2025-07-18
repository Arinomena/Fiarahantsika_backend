package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.PackagingDTO;
import java.util.List;

public interface IPackagingService {
    List<PackagingDTO> getAllPackagings();
    PackagingDTO getPackagingById(Long id);
    PackagingDTO createPackaging(PackagingDTO dto);
    PackagingDTO updatePackaging(Long id, PackagingDTO dto);
    void deletePackaging(Long id);
}
