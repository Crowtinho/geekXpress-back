package com.geek.back.repositories;

import com.geek.back.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategories_NameIgnoreCase(String categoryName);
=======
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByName(String name);
>>>>>>> 5c86157b7f16e60c5991812b5319f7c817b5e7eb
}

