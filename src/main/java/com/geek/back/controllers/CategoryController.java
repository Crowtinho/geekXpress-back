package com.geek.back.controllers;


import com.geek.back.entities.Category;
import com.geek.back.services.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    final private CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @Transactional(readOnly = true)
    @GetMapping
    public ResponseEntity<List<Category>> list(){
        return ResponseEntity.of(Optional.ofNullable(categoryService.findAll()));
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public ResponseEntity<Category> details(@PathVariable Long id){
        Optional<Category> optionalCategory = categoryService.findById(id);
        if (optionalCategory.isPresent()){
            return ResponseEntity.ok(optionalCategory.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<Category> create(@Valid @RequestBody Category category){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(category));
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<Category> update(@Valid @RequestBody Category category, @PathVariable Long id){

        return ResponseEntity.ok(categoryService.updateCategory(id,category));

    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        categoryService.deleteById(id); // Se elimina la categoría
        return ResponseEntity.noContent().build(); // HTTP 204
    }



}
