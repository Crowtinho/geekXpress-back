package com.geek.back.services;

import com.geek.back.models.Product;
import com.geek.back.models.ProductImage;
import com.geek.back.repositories.ProductImageRepository;
import com.geek.back.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    final private ProductRepository productRepository;
    final private ProductImageRepository productImageRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    @Override
    public Optional<Product> deleteById(Long id) {
        return productRepository.findById(id).map(p -> {
            productRepository.deleteById(id);
            return p;
        });
    }


    @Transactional
    @Override
    public Product addImageToProduct(Long productId, ProductImage image) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        boolean exist = product.getImages().stream()
                .anyMatch(img -> img.getUrl().equals(image.getUrl()));

        if (!exist) {
            product.addImage(image);
        }
        return productRepository.save(product);
    }

    @Transactional
    @Override
    public Product removeImageFromProduct(Long productId, Long imageId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image not found"));

        product.removeImage(image);
        return productRepository.save(product);
    }

    @Override
    public List<Product> findByCategoryName(String categoryName) {
        return productRepository.findByCategories_NameIgnoreCase(categoryName);
    }

}

