package com.geek.back.repositories;

import com.geek.back.models.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ImageProduct,Long> {
}
