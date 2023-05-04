package com.autosale.shop.repository;

import com.autosale.shop.model.Pagination;
import com.autosale.shop.model.Product;
import org.jooq.Condition;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Integer> save(Product product);

    Optional<Product> findById(int id);

    void update(Product product);

    int deleteById(int id);

    List<Product> findAllWithConditions(Pagination pagination, List<Condition> conditions);
}
