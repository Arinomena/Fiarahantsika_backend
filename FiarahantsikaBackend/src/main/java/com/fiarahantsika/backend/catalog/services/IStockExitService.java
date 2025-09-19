package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.CreateStockExitRequest;
import com.fiarahantsika.backend.catalog.dto.StockExitDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IStockExitService {
    StockExitDTO recordExit(CreateStockExitRequest request);
    List<StockExitDTO> getExits(Long productId);
    Page<StockExitDTO> getExitsPage(Long productId, Pageable pageable);
}
