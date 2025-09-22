package com.geek.back.controllers;

import com.geek.back.models.ImageProduct;
import com.geek.back.services.ProductImageServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/images")
public class ProductImageController {

    final private ProductImageServiceImpl imageService;

    public ProductImageController(ProductImageServiceImpl imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<List<ImageProduct>> list(){
        return ResponseEntity.of(Optional.ofNullable(imageService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageProduct> details(@PathVariable Long id){
        Optional<ImageProduct> image = imageService.findById(id);
        if (image.isPresent()){
            return ResponseEntity.ok(image.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<ImageProduct> create(@Valid @RequestBody ImageProduct image){
       return ResponseEntity.status(HttpStatus.CREATED).body(imageService.save(image));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ImageProduct> update(@Valid @RequestBody ImageProduct image, @PathVariable Long id){
        Optional<ImageProduct> imageOptional = imageService.findById(id);
        if (imageOptional.isPresent()){
            ImageProduct imageDb = imageOptional.orElseThrow();
            imageDb.setUrl(image.getUrl());
            imageDb.setMainImage(image.isMainImage());
            imageDb.setProduct(image.getProduct());

            return ResponseEntity.ok(imageService.save(imageDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ImageProduct> delete(@PathVariable Long id){
        Optional<ImageProduct> imageOptional = imageService.deleteById(id);
        if (imageOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(imageOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
}
