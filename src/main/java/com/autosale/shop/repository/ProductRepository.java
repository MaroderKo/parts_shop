package com.autosale.shop.repository;

import com.autosale.shop.model.PaginationRequest;
import com.autosale.shop.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Integer> save(Product product);

    Optional<Product> findById(int id);

    void update(Product product);

    int deleteById(int id);

    int deleteByIdInArray(List<Integer> id);

    List<Product> findAllByUserId(int userId);

    List<Product> findAllByStatus(PaginationRequest pageRequest, String status);

    int countAllByStatus(PaginationRequest pageRequest, String status);

    void saveAllIgnoreExistence(List<Product> product);
}
