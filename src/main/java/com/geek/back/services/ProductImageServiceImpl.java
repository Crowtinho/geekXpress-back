package com.geek.back.services;

import com.geek.back.models.ImageProduct;
import com.geek.back.repositories.ProductImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductImageServiceImpl implements ProductImageService{

    final private ProductImageRepository imageRepository;

    public ProductImageServiceImpl(ProductImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ImageProduct> findAll() {
        return imageRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ImageProduct> findById(Long id) {
        return imageRepository.findById(id);
    }

    @Transactional
    @Override
    public ImageProduct save(ImageProduct imageProduct) {
        return imageRepository.save(imageProduct);
    }

    @Transactional
    @Override
    public Optional<ImageProduct> deleteById(Long id) {
        return imageRepository.findById(id).map(i ->{
            imageRepository.deleteById(id);
            return i;
        });
    }

}
