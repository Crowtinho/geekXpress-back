package com.geek.back.controllers;

import com.geek.back.dtos.CartItemDTO;
import com.geek.back.dtos.CartItemRequestDTO;
import com.geek.back.dtos.ShoppingCartDTO;
import com.geek.back.services.ShoppingCartServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartServiceImpl cartService;

    public ShoppingCartController(ShoppingCartServiceImpl cartService) {
        this.cartService = cartService;
    }
    @GetMapping("/{userId}/items")
    public ResponseEntity<List<CartItemDTO>> getCartItems(@PathVariable Long userId) {
        List<CartItemDTO> items = cartService.getCartItems(userId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ShoppingCartDTO> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<CartItemDTO> addProduct(@PathVariable Long userId,
                                                  @Valid @RequestBody CartItemRequestDTO request) {
        return ResponseEntity.ok(cartService.addProductToCart(userId, request));
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<CartItemDTO> updateProduct(@PathVariable Long userId,
                                                     @Valid @RequestBody CartItemRequestDTO request) {
        return ResponseEntity.ok(cartService.updateCartItem(userId, request));
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable Long userId, @PathVariable Long productId) {
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

}

