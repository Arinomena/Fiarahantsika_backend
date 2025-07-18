// src/main/java/com/fiarahantsika/backend/catalog/controllers/StockEntryController.java
package com.fiarahantsika.backend.catalog.controllers;

import com.fiarahantsika.backend.catalog.dto.CreateStockEntryRequest;
import com.fiarahantsika.backend.catalog.dto.StockEntryDTO;
import com.fiarahantsika.backend.catalog.services.IStockEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/stock/entries")
@RequiredArgsConstructor
public class StockEntryController {

    private final IStockEntryService stockEntryService;

    @PostMapping
    public ResponseEntity<StockEntryDTO> recordEntry(
            @RequestBody CreateStockEntryRequest req
    ) {
        StockEntryDTO dto = stockEntryService.recordEntry(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public List<StockEntryDTO> listEntries(
            @RequestParam(required = false) Long productId
    ) {
        return stockEntryService.getEntries(productId);
    }
}
