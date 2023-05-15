package com.autosale.shop.repository;

import com.autosale.shop.model.Product;

import java.util.List;

public interface AmazonS3ProductRepository {
    void save(List<Product> products);

    List<Product> load(String name);
}
