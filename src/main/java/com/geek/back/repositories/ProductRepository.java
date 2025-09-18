package com.geek.back.repositories;

import com.geek.back.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategories_NameIgnoreCase(String categoryName);
}

