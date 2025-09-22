package com.geek.back.repositories;

import com.geek.back.entities.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    Optional<ShoppingCart> findByUserId(Long userId);
}
