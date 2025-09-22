package com.geek.back.repositories;

import com.geek.back.entities.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageProductRepository extends JpaRepository<ImageProduct,Long> {
    List<ImageProduct> findByProductId(Long productId);
}
