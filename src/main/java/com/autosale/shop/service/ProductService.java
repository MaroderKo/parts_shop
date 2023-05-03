package com.autosale.shop.service;

import com.autosale.shop.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    List<Product> findAll();

    List<Product> findAllActive();

    Product findById(int id);

    List<Product> findAllFromCurrentUser();

    List<Product> findAllFromUser(int id);

    int create(Product product);

    void edit(Product product);

    int deleteById(int id);

    void makeSold(int productId);
}
