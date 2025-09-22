package com.geek.back.services;

import java.util.List;
import java.util.Optional;

public interface CrudService<T>{
    List<T> findAll();
    Optional<T> findById(Long id);
    void deleteById(Long id);
}
