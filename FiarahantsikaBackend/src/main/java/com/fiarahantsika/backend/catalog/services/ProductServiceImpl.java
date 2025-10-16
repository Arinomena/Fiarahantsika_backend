package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.ProductDTO;
import com.fiarahantsika.backend.catalog.entities.Product;
import com.fiarahantsika.backend.catalog.mappers.ProductMapper;
import com.fiarahantsika.backend.catalog.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements IProductService {

    private final ProductRepository repo;

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProducts(String search) {
        var entities = (search == null || search.isBlank())
                ? repo.findAll()
                : repo.findByNameContainingIgnoreCase(search);
        return entities.stream()
                .map(ProductMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé : " + id));
        return ProductMapper.toDto(p);
    }

    @Override
    public ProductDTO createProduct(ProductDTO dto) {
        Product p = ProductMapper.toEntity(dto);
        p.setCurrentStock(0);
        Product saved = repo.save(p);
        return ProductMapper.toDto(saved);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé : " + id));
        ProductMapper.updateEntity(dto, p);
        Product updated = repo.save(p);
        return ProductMapper.toDto(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProductsPage(Pageable pageable) {
        return repo.findAll(pageable).map(ProductMapper::toDto);
    }
}
