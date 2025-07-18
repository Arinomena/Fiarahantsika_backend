package com.fiarahantsika.backend.catalog.controllers;

import com.fiarahantsika.backend.catalog.dto.CreateStockExitRequest;
import com.fiarahantsika.backend.catalog.dto.StockExitDTO;
import com.fiarahantsika.backend.catalog.services.IStockExitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/catalog/stock/exits")
@RequiredArgsConstructor
public class StockExitController {

    private final IStockExitService stockExitService;

    @PostMapping
    public ResponseEntity<StockExitDTO> recordExit(@RequestBody CreateStockExitRequest req) {
        StockExitDTO dto = stockExitService.recordExit(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public List<StockExitDTO> listExits(@RequestParam(required = false) Long productId) {
        return stockExitService.getExits(productId);
    }
}
