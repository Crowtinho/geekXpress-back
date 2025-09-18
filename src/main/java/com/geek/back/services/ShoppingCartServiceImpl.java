package com.geek.back.services;

import com.geek.back.dto.CartItemDTO;
import com.geek.back.dto.ShoppingCartDTO;
import com.geek.back.models.CartItem;
import com.geek.back.models.Product;
import com.geek.back.models.ShoppingCart;
import com.geek.back.models.User;
import com.geek.back.repositories.ProductRepository;
import com.geek.back.repositories.ShoppingCartRepository;
import com.geek.back.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository,
                                   UserRepository userRepository,
                                   ProductRepository productRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // Convierte ShoppingCart a DTO
    private ShoppingCartDTO toDTO(ShoppingCart cart) {
        List<CartItemDTO> items = cart.getDetails().stream()
                .map(item -> new CartItemDTO(
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getUnitPrice()
                )).collect(Collectors.toList());

        return new ShoppingCartDTO(cart.getUser().getId(), items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartDTO> findAll() {
        return shoppingCartRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ShoppingCartDTO> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public ShoppingCartDTO save(ShoppingCartDTO dto) {
        ShoppingCart cart = fromDTO(dto);
        ShoppingCart saved = shoppingCartRepository.save(cart);
        return toDTO(saved);
    }

    @Override
    public Optional<ShoppingCartDTO> deleteById(Long id) {
        return shoppingCartRepository.findById(id).map(cart -> {
            shoppingCartRepository.delete(cart);
            return toDTO(cart);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartDTO> findByUsuarioId(Long usuarioId) {
        return shoppingCartRepository.findAll().stream()
                .filter(cart -> cart.getUser().getId().equals(usuarioId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ShoppingCart createCart(ShoppingCartDTO dto) {
        return shoppingCartRepository.save(fromDTO(dto));
    }

    // Convierte DTO a entidad ShoppingCart
    private ShoppingCart fromDTO(ShoppingCartDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);

        Set<CartItem> items = new HashSet<>();
        for (CartItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

            CartItem item = CartItem.builder()
                    .shoppingCart(cart)
                    .product(product)
                    .quantity(itemDTO.getQuantity())
                    .unitPrice(itemDTO.getUnitPrice())
                    .build();

            items.add(item);
        }

        cart.setDetails(items);
        return cart;
    }
}
