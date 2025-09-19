package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.CreateStockExitRequest;
import com.fiarahantsika.backend.catalog.dto.StockExitDTO;
import com.fiarahantsika.backend.catalog.entities.Product;
import com.fiarahantsika.backend.catalog.entities.StockExit;
import com.fiarahantsika.backend.catalog.repositories.ProductRepository;
import com.fiarahantsika.backend.catalog.repositories.StockExitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StockExitServiceImpl implements IStockExitService {

    private final StockExitRepository stockExitRepo;
    private final ProductRepository   productRepo;
    private final IStockAlertService  alertService;

    @Override
    public StockExitDTO recordExit(CreateStockExitRequest req) {
        Product product = productRepo.findById(req.productId())
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable : " + req.productId()));

        int qty = req.quantity();
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantité doit être > 0");
        }
        if (product.getCurrentStock() < qty) {
            throw new IllegalArgumentException("Stock insuffisant");
        }

        product.setCurrentStock(product.getCurrentStock() - qty);
        productRepo.save(product);

        StockExit exit = new StockExit();
        exit.setProduct(product);
        exit.setQuantity(qty);
        exit.setExitDate(Instant.now());
        exit.setRemainingStock(product.getCurrentStock());

        StockExit saved = stockExitRepo.save(exit);

        alertService.checkAndCreateAlert(
                product.getId(),
                product.getCurrentStock(),
                product.getSeuil()
        );

        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockExitDTO> getExits(Long productId) {
        var list = (productId != null)
                ? stockExitRepo.findByProductId(productId)
                : stockExitRepo.findAll();
        return list.stream().map(StockExitServiceImpl::toDto).toList();
    }

    private static StockExitDTO toDto(StockExit e) {
        return new StockExitDTO(
                e.getId(),
                e.getProduct().getId(),
                e.getQuantity(),
                e.getExitDate(),
                e.getRemainingStock()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockExitDTO> getExitsPage(Long productId, Pageable pageable) {
        return (productId != null)
                ? stockExitRepo.findByProductId(productId, pageable).map(StockExitServiceImpl::toDto)
                : stockExitRepo.findAll(pageable).map(StockExitServiceImpl::toDto);
    }
}
