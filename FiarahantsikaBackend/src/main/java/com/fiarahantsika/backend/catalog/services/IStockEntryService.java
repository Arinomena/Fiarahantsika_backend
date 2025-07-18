package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.CreateStockEntryRequest;
import com.fiarahantsika.backend.catalog.dto.StockEntryDTO;
import java.util.List;

public interface IStockEntryService {
    StockEntryDTO recordEntry(CreateStockEntryRequest request);
    List<StockEntryDTO> getEntries(Long productId);
}
