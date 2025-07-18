package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.StockAlertDTO;
import com.fiarahantsika.backend.catalog.entities.Product;
import com.fiarahantsika.backend.catalog.entities.StockAlert;
import com.fiarahantsika.backend.catalog.repositories.ProductRepository;
import com.fiarahantsika.backend.catalog.repositories.StockAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StockAlertServiceImpl implements IStockAlertService {

    private final StockAlertRepository alertRepo;
    private final ProductRepository     productRepo;

    @Override
    @Transactional(readOnly = true)
    public List<StockAlertDTO> getAlerts() {
        return alertRepo.findByResolvedFalse().stream().map(alert -> {
            Product p = alert.getProduct();
            return new StockAlertDTO(
                    p.getId(),
                    p.getName(),
                    p.getCurrentStock(),
                    p.getSeuil()
            );
        }).toList();
    }

    @Override
    public void checkAndCreateAlert(Long productId, Integer currentStock, Integer seuil) {
        if (currentStock < seuil && !alertRepo.existsByProductIdAndResolvedFalse(productId)) {
            Product p = productRepo.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Produit introuvable : " + productId));
            StockAlert alert = new StockAlert();
            alert.setProduct(p);
            alertRepo.save(alert);
        }
    }

    @Override
    public void resolveAlert(Long productId) {
        alertRepo.findByResolvedFalse().stream()
                .filter(a -> a.getProduct().getId().equals(productId))
                .forEach(alert -> {
                    alert.setResolved(true);
                    alert.setResolvedAt(Instant.now());
                    alertRepo.save(alert);
                });
    }
}