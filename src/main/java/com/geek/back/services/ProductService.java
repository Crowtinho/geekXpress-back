package com.geek.back.services;

import com.geek.back.dto.ProductDTO;
import com.geek.back.models.Product;
import com.geek.back.models.ImageProduct;

public interface ProductService extends Service<ProductDTO> {

    Product addImageToProduct(Long productId, ImageProduct image);
    Product removeImageFromProduct(Long productId, Long imageId);

}
