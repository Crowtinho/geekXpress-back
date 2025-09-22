package com.geek.back.services;

import com.geek.back.dtos.CartItemDTO;
import com.geek.back.dtos.CartItemRequestDTO;
import com.geek.back.dtos.ShoppingCartDTO;

import java.util.List;

public interface ShoppingCartService {

    List<CartItemDTO> getCartItems(Long userId);

    ShoppingCartDTO getCartByUserId(Long userId);

    CartItemDTO addProductToCart(Long userId, CartItemRequestDTO request);

    CartItemDTO updateCartItem(Long userId, CartItemRequestDTO request);

    void removeProductFromCart(Long userId, Long productId);
}
