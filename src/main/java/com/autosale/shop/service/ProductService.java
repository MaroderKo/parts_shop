package com.autosale.shop.service;

import com.autosale.shop.model.PaginationRequest;
import com.autosale.shop.model.PaginationResponse;
import com.autosale.shop.model.Product;
import com.autosale.shop.model.ProductStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    PaginationResponse<Product> findByStatus(PaginationRequest paginationRequest, ProductStatus status);

    Product findById(int id);

    List<Product> findAllFromCurrentUser();

    List<Product> findAllByUserId(int id);

    int create(Product product);

    void edit(Product product);

    int deleteById(int id);

    void buy(int productId);
}
