package com.geek.back.services;

import com.geek.back.dtos.ProductDTO;
import com.geek.back.dtos.ProductRequestDTO;

public interface ProductService extends CrudService<ProductDTO>{
    ProductDTO createProduct(ProductRequestDTO request);
    ProductDTO updateProduct(Long productId, ProductRequestDTO request);
}
