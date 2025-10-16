package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    List<ProductDTO> getProducts(String search);
    ProductDTO getProductById(Long id);
    ProductDTO createProduct(ProductDTO dto);
    ProductDTO updateProduct(Long id, ProductDTO dto);
    void deleteProduct(Long id);
    Page<ProductDTO> getProductsPage(Pageable pageable);
}
