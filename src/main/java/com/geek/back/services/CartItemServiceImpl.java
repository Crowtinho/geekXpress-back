package com.geek.back.services;

import com.geek.back.dto.CartItemDTO;
import com.geek.back.models.CartItem;
import com.geek.back.models.Product;
import com.geek.back.models.ShoppingCart;
import com.geek.back.repositories.CartItemRepository;
import com.geek.back.repositories.ProductRepository;
import com.geek.back.repositories.ShoppingCartRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, ShoppingCartRepository shoppingCartRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> findAll() {
        return cartItemRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CartItem> findById(Long id) {
        return cartItemRepository.findById(id);
    }

    @Override
    public CartItem save(CartItem detail) {
        return cartItemRepository.save(detail);
    }

    @Override
    public Optional<CartItem> deleteById(Long id) {
        return cartItemRepository.findById(id).map(d -> {
            cartItemRepository.deleteById(id);
            return d;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> findByCarritoId(Long cartId) {
        return cartItemRepository.findByShoppingCart_Id(cartId);
    }

    @Transactional
    public CartItem addItemToCart(Long cartId, CartItemDTO dto) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Si ya existe el item con el mismo producto, incrementa cantidad
        CartItem existingItem = cart.getDetails().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + dto.getQuantity());
            existingItem.setUnitPrice(product.getPrice()); // opcional actualizar precio
            return cartItemRepository.save(existingItem);
        }

        CartItem newItem = CartItem.builder()
                .shoppingCart(cart)
                .product(product)
                .quantity(dto.getQuantity())
                .unitPrice(product.getPrice())
                .build();

        return cartItemRepository.save(newItem);
    }

}
