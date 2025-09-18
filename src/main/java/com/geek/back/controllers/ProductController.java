package com.geek.back.controllers;

import com.geek.back.dto.ProductDTO;
import com.geek.back.models.Product;
import com.geek.back.models.ProductImage;
import com.geek.back.services.CategoryServiceImpl;
import com.geek.back.services.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductServiceImpl productService;
    private final CategoryServiceImpl categoryService;

    public ProductController(ProductServiceImpl productService, CategoryServiceImpl categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // Listar todos los productos
    @GetMapping
    public ResponseEntity<List<ProductDTO>> list() {
        return ResponseEntity.ok(productService.findAll());
    }

    // Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> details(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un producto
    @PostMapping
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO dto) {
        ProductDTO savedProduct = productService.save(dto);  // 👈 delega al servicio
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    // Actualizar un producto (sin sobrescribir imágenes)
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return productService.saveOrUpdate(dto, id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    // Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return productService.deleteById(id)
                .map(p -> ResponseEntity.noContent().<Void>build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Agregar una imagen a un producto
    @PostMapping("/{id}/images")
    public ResponseEntity<Product> addImage(@PathVariable Long id, @RequestBody ProductImage image) {
        Product updatedProduct = productService.addImageToProduct(id, image);
        return ResponseEntity.ok(updatedProduct);
    }

    // Eliminar una imagen de un producto
    @DeleteMapping("/{productId}/images/{imageId}")
    public ResponseEntity<Product> removeImage(@PathVariable Long productId, @PathVariable Long imageId) {
        Product updatedProduct = productService.removeImageFromProduct(productId, imageId);
        return ResponseEntity.ok(updatedProduct);
    }
}
