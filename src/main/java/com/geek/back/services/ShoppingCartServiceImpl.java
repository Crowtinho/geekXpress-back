package com.geek.back.services;

import com.geek.back.dtos.CartItemDTO;
import com.geek.back.dtos.CartItemRequestDTO;
import com.geek.back.dtos.ShoppingCartDTO;
import com.geek.back.entities.CartItem;
import com.geek.back.entities.Product;
import com.geek.back.entities.ShoppingCart;
import com.geek.back.repositories.CartItemRepository;
import com.geek.back.repositories.ProductRepository;
import com.geek.back.repositories.ShoppingCartRepository;
import com.geek.back.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository cartRepository,
                                   CartItemRepository cartItemRepository,
                                   ProductRepository productRepository,
                                   UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemDTO> getCartItems(Long userId) {
        ShoppingCart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return cart.getItems().stream()
                .map(this::toDTO)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDTO getCartByUserId(Long userId) {
        ShoppingCart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return toDTO(cart);
    }

    @Override
    @Transactional
    public CartItemDTO addProductToCart(Long userId, CartItemRequestDTO request) {
        // Obtener el carrito del usuario
        ShoppingCart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Obtener el producto
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Buscar si el producto ya está en el carrito
        Optional<CartItem> existingItemOpt = cartItemRepository
                .findByShoppingCartIdAndProductId(cart.getId(), product.getId());

        CartItem cartItem;
        if (existingItemOpt.isPresent()) {
            // Si ya existe, solo aumentar la cantidad
            cartItem = existingItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            // Si no existe, crear un nuevo CartItem
            cartItem = new CartItem();
            cartItem.setShoppingCart(cart);
            cartItem.setProduct(product);
            cartItem.setUnitPrice(product.getPrice());
            cartItem.setQuantity(request.getQuantity());
        }

        // Guardar y devolver DTO
        return toDTO(cartItemRepository.save(cartItem));
    }


    @Override
    @Transactional
    public CartItemDTO updateCartItem(Long userId, CartItemRequestDTO request) {
        ShoppingCart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem cartItem = cartItemRepository
                .findByShoppingCartIdAndProductId(cart.getId(), request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not in cart"));

        if (request.getQuantity() <= 0) {
            cartItemRepository.delete(cartItem);
            return null;
        }

        cartItem.setQuantity(request.getQuantity());
        return toDTO(cartItemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public void removeProductFromCart(Long userId, Long productId) {
        ShoppingCart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem cartItem = cartItemRepository
                .findByShoppingCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Product not in cart"));

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void clearCart(Long userId) {
        ShoppingCart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cartItemRepository.deleteAllByShoppingCartId(cart.getId());
    }


    private CartItemDTO toDTO(CartItem cartItem) {
        if (cartItem == null) return null;

        String imageUrl = "";
        if (cartItem.getProduct().getImageProducts() != null && !cartItem.getProduct().getImageProducts().isEmpty()) {
            imageUrl = cartItem.getProduct().getImageProducts().get(0).getUrl(); // primera imagen
        }
        return CartItemDTO.builder()
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .imageUrl(imageUrl)
                .build();
    }

    private ShoppingCartDTO toDTO(ShoppingCart cart) {
        if (cart == null) return null;
        List<CartItemDTO> items = cartItemRepository.findAllByShoppingCartId(cart.getId())
                .stream()
                .map(this::toDTO)
                .toList();
        return ShoppingCartDTO.builder()
                .userId(cart.getUser().getId())
                .items(items)
                .build();
    }
}

