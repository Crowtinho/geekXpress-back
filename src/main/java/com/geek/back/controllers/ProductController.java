package com.geek.back.controllers;

import com.geek.back.models.Product;
import com.geek.back.models.ProductImage;
import com.geek.back.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:8080/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ✅ Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok(productService.findAll());
    }

    // ✅ Obtener producto por id
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Crear producto
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.ok(productService.save(product));
    }

    // ✅ Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        return productService.findById(id).map(p -> {
            product.setId(id);
            return ResponseEntity.ok(productService.save(product));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> delete(@PathVariable Long id) {
        return productService.deleteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Agregar imagen a producto
    @PostMapping("/{id}/images")
    public ResponseEntity<Product> addImage(@PathVariable Long id, @RequestBody ProductImage image) {
        try {
            return ResponseEntity.ok(productService.addImageToProduct(id, image));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Eliminar imagen de producto
    @DeleteMapping("/{id}/images/{imageId}")
    public ResponseEntity<Product> removeImage(@PathVariable Long id, @PathVariable Long imageId) {
        try {
            return ResponseEntity.ok(productService.removeImageFromProduct(id, imageId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Nuevo: Filtrar productos por categoría
    @GetMapping("/category/{name}")
    public ResponseEntity<List<Product>> listByCategory(@PathVariable String name) {
        return ResponseEntity.ok(productService.findByCategoryName(name));
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<Product>> findByCategory(@PathVariable String categoryName) {
        return ResponseEntity.ok(productService.findByCategoryName(categoryName));
    }

}
