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
    public List<StockAlertDTO> getAlerts(Boolean resolved) {
        List<StockAlert> alerts;
        if (resolved == null) {
            alerts = alertRepo.findAll();
        } else if (resolved) {
            alerts = alertRepo.findByResolvedTrue();
        } else {
            alerts = alertRepo.findByResolvedFalse();
        }

        return alerts.stream()
                .map(alert -> {
                    Product p = alert.getProduct();
                    return new StockAlertDTO(
                            alert.getId(),
                            p.getId(),
                            p.getName(),
                            p.getCurrentStock(),
                            p.getSeuil(),
                            alert.getCreatedAt(),
                            alert.getResolved(),
                            alert.getResolvedAt()
                    );
                })
                .toList();
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

    @Override
    @Transactional
    public StockAlertDTO setResolved(Long alertId, boolean flag) {
        StockAlert alert = alertRepo.findById(alertId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Aucune alerte trouvée pour l’ID " + alertId));

        alert.setResolved(flag);
        alert.setResolvedAt(flag ? Instant.now() : null);
        return toDto(alertRepo.save(alert));
    }

    private StockAlertDTO toDto(StockAlert e) {
        return new StockAlertDTO(
                e.getId(),
                e.getProduct().getId(),
                e.getProduct().getName(),
                e.getProduct().getCurrentStock(),
                e.getProduct().getSeuil(),
                e.getCreatedAt(),
                e.getResolved(),
                e.getResolvedAt()
        );
    }

}