package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.CreateStockEntryRequest;
import com.fiarahantsika.backend.catalog.dto.StockEntryDTO;
import com.fiarahantsika.backend.catalog.entities.Product;
import com.fiarahantsika.backend.catalog.entities.StockEntry;
import com.fiarahantsika.backend.catalog.repositories.ProductRepository;
import com.fiarahantsika.backend.catalog.repositories.StockEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StockEntryServiceImpl implements IStockEntryService {

    private final StockEntryRepository entryRepo;
    private final ProductRepository    productRepo;
    private final IStockAlertService    alertService;

    @Override
    public StockEntryDTO recordEntry(CreateStockEntryRequest req) {
        Product p = productRepo.findById(req.productId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Produit introuvable : " + req.productId()));

        int groups = req.quantity();
        if (groups <= 0) {
            throw new IllegalArgumentException("quantity doit être > 0");
        }

        int sizePerGroup = p.getGroupSize();
        int bottlesToAdd = groups * sizePerGroup;

        p.setCurrentStock(p.getCurrentStock() + bottlesToAdd);
        productRepo.save(p);

        StockEntry e = new StockEntry();
        e.setProduct(p);
        e.setQuantity(groups);
        e.setEntryDate(Instant.now());
        e.setRemainingStock(p.getCurrentStock());
        StockEntry saved = entryRepo.save(e);

        alertService.resolveAlert(p.getId());

        return new StockEntryDTO(
                saved.getId(),
                p.getId(),
                groups,
                bottlesToAdd,     // bottleQty
                saved.getEntryDate(),
                p.getCurrentStock()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockEntryDTO> getEntries(Long productId) {
        List<StockEntry> entities = (productId != null)
                ? entryRepo.findByProductId(productId)
                : entryRepo.findAll();

        return entities.stream()
                .map(e -> {
                    int groups        = e.getQuantity();
                    int sizePerGroup  = e.getProduct().getGroupSize();
                    int bottles       = groups * sizePerGroup;
                    int remainingStock = e.getProduct().getCurrentStock();  // ← déclaration

                    return new StockEntryDTO(
                            e.getId(),
                            e.getProduct().getId(),
                            groups,
                            bottles,
                            e.getEntryDate(),
                            remainingStock                                    // ← passe la variable
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockEntryDTO> getEntriesPage(Long productId, Pageable pageable) {
        Page<StockEntry> pageResult = (productId != null)
                ? entryRepo.findByProductId(productId, pageable)
                : entryRepo.findAll(pageable);

        return pageResult.map(e -> {
            int groups         = e.getQuantity();
            int sizePerGroup   = e.getProduct().getGroupSize();
            int bottles        = groups * sizePerGroup;
            int remainingStock = e.getProduct().getCurrentStock();

            return new StockEntryDTO(
                    e.getId(),
                    e.getProduct().getId(),
                    groups,
                    bottles,
                    e.getEntryDate(),
                    remainingStock
            );
        });
    }
}
