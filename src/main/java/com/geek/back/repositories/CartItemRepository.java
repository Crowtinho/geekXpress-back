package com.geek.back.repositories;

import com.geek.back.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findAllByShoppingCartId(Long cartId);
    Optional<CartItem> findByShoppingCartIdAndProductId(Long cartId, Long productId);
    void deleteAllByShoppingCartId(Long cartId); // sin @Transactional

}
