package com.geek.back.services;

import com.geek.back.entities.Category;

public interface CategoryService extends CrudService<Category> {

    Category createCategory(Category category);
    Category updateCategory(Long id, Category category);
}
