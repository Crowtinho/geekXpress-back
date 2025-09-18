package com.geek.back.repositories;

import com.geek.back.dto.ShoppingCartDTO;
import com.geek.back.models.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    List<ShoppingCart> findByUserId(Long userId);
}
