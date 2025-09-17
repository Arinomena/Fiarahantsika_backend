package com.fiarahantsika.backend.stats.controller;

import com.fiarahantsika.backend.stats.dto.*;
import com.fiarahantsika.backend.stats.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/sales-by-day")
    public List<SalesByDayDTO> salesByDay() {
        return statsService.getSalesByDay();
    }

    @GetMapping("/top-products")
    public List<TopProductDTO> topProducts() {
        return statsService.getTopProducts();
    }

    @GetMapping("/invoice-status")
    public List<InvoiceStatusDTO> invoiceStatus() {
        return statsService.getInvoiceStatusStats();
    }

    @GetMapping("/average-basket")
    public AverageBasketDTO averageBasket() {
        return statsService.getAverageBasket();
    }

    @GetMapping("/sales-by-client")
    public List<SalesByClientDTO> salesByClient() {
        return statsService.getSalesByClient();
    }

    @GetMapping("/sales-by-seller/current-month")
    public List<SalesBySellerDTO> salesBySellerCurrentMonth() {
        return statsService.getSalesBySellerCurrentMonth();
    }

    @GetMapping("/sales-by-seller")
    public List<SalesBySellerDTO> salesBySellerBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return statsService.getSalesBySellerBetween(start, end);
    }
}
