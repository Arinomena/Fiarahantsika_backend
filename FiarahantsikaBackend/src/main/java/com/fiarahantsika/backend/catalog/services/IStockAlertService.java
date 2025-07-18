package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.StockAlertDTO;
import java.util.List;

public interface IStockAlertService {
    List<StockAlertDTO> getAlerts();
    void checkAndCreateAlert(Long productId, Integer currentStock, Integer seuil);
    void resolveAlert(Long productId);
}