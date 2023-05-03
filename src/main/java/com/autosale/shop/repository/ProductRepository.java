package com.autosale.shop.repository;

import com.autosale.shop.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Integer> save(Product product);

    Optional<Product> findById(int id);

    void update(Product product);

    int deleteById(int id);

    List<Product> findAllActive();

    List<Product> findAll();

    List<Product> findAllByUserId(int id);
}
