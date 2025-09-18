package com.geek.back.controllers;

import com.geek.back.models.CartItem;
import com.geek.back.services.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart-details")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> list() {
        return ResponseEntity.ok(cartItemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItem> details(@PathVariable Long id) {
        return cartItemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/carrito/{carritoId}")
    public ResponseEntity<List<CartItem>> listByCarrito(@PathVariable Long carritoId) {
        return ResponseEntity.ok(cartItemService.findByCarritoId(carritoId));
    }

    @PostMapping("/create")
    public ResponseEntity<CartItem> create(@RequestBody CartItem detalle) {
        CartItem saved = cartItemService.save(detalle);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<CartItem> update(@PathVariable Long id, @RequestBody CartItem detail) {
        detail.setId(id);
        CartItem saved = cartItemService.save(detail);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cartItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
