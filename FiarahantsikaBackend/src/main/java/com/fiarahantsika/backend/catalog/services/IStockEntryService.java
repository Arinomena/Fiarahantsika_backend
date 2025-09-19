package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.CreateStockEntryRequest;
import com.fiarahantsika.backend.catalog.dto.StockEntryDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IStockEntryService {
    StockEntryDTO recordEntry(CreateStockEntryRequest request);
    List<StockEntryDTO> getEntries(Long productId);
    Page<StockEntryDTO> getEntriesPage(Long productId, Pageable pageable);
}
