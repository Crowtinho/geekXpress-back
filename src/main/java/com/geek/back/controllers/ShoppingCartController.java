package com.geek.back.controllers;

import com.geek.back.dto.ShoppingCartDTO;
import com.geek.back.models.ShoppingCart;
import com.geek.back.services.ShoppingCartService;
import com.geek.back.services.ShoppingCartServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartServiceImpl shoppingCartService;

    public ShoppingCartController(ShoppingCartServiceImpl shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingCartDTO>> list() {
        return ResponseEntity.ok(shoppingCartService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCartDTO> details(@PathVariable Long id) {
        return shoppingCartService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{usuarioId}")
    public ResponseEntity<List<ShoppingCartDTO>> listByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(shoppingCartService.findByUsuarioId(usuarioId));
    }

    @PostMapping("/create")
    public ResponseEntity<ShoppingCartDTO> create(@RequestBody ShoppingCartDTO shoppingCart) {
        ShoppingCartDTO saved = shoppingCartService.save(shoppingCart);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ShoppingCartDTO> update(@PathVariable Long id, @RequestBody ShoppingCartDTO shoppingCart) {
        shoppingCart.setUserId(id);
        ShoppingCartDTO saved = shoppingCartService.save(shoppingCart);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        shoppingCartService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
