package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.CreateStockExitRequest;
import com.fiarahantsika.backend.catalog.dto.StockExitDTO;
import java.util.List;

public interface IStockExitService {
    StockExitDTO recordExit(CreateStockExitRequest request);
    List<StockExitDTO> getExits(Long productId);
}
