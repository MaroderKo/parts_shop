package com.autosale.shop.service;

import com.autosale.shop.model.Pagination;
import com.autosale.shop.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    List<Product> findAll(Pagination pagination);

    List<Product> findByStatus(Pagination pagination, String status);

    Product findById(int id);

    List<Product> findAllFromCurrentUser(Pagination pagination);

    List<Product> findAllByUserId(int id, Pagination pagination);

    int create(Product product);

    void edit(Product product);

    int deleteById(int id);

    void buy(int productId);
}
