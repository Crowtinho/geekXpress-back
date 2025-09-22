package com.geek.back.services;

import com.geek.back.entities.Category;
import com.geek.back.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    final private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        return  categoryRepository.findById(id).map(c ->{
            c.setName(category.getName());
            return categoryRepository.save(c);
        }).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.findById(id).map(c ->{
            categoryRepository.deleteById(id);
            return c;
        });
    }
}
