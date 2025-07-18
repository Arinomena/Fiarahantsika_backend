package com.fiarahantsika.backend.catalog.services;

import com.fiarahantsika.backend.catalog.dto.ProductDTO;
import java.util.List;

public interface IProductService {
    List<ProductDTO> getProducts(String search);
    ProductDTO       getProductById(Long id);
    ProductDTO       createProduct(ProductDTO dto);
    ProductDTO       updateProduct(Long id, ProductDTO dto);
    void             deleteProduct(Long id);
}
