package com.fiarahantsika.backend.stats.service;

import com.fiarahantsika.backend.stats.dto.*;
import com.fiarahantsika.backend.stats.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    public List<SalesByDayDTO> getSalesByDay() {
        return statsRepository.getSalesByDay();
    }

    public List<TopProductDTO> getTopProducts() {
        return statsRepository.getTopProducts();
    }

    public List<InvoiceStatusDTO> getInvoiceStatusStats() {
        return statsRepository.getInvoiceStatusStats();
    }

    public AverageBasketDTO getAverageBasket() {
        return statsRepository.getAverageBasket();
    }

    public List<SalesByClientDTO> getSalesByClient() {
        return statsRepository.getSalesByClient();
    }

    public List<SalesBySellerDTO> getSalesBySellerCurrentMonth() {
        return statsRepository.getSalesBySellerCurrentMonth();
    }

    public List<SalesBySellerDTO> getSalesBySellerBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return statsRepository.getSalesBySellerBetween(startDate, endDate);
    }
}
