package com.autosale.shop.service.impl;

import com.autosale.shop.model.Product;
import com.autosale.shop.model.ProductStatus;
import com.autosale.shop.repository.ProductRepository;
import com.autosale.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Product> findAllActive() {
        return repository.findAllActive();
    }

    @Override
    public Product findById(int id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Product with id %d not found", id)));
    }

    @Override
    public List<Product> findAllFromCurrentUser() {
        return repository.findAllByUserId((Integer) getContext().getAuthentication().getPrincipal());
    }

    @Override
    public List<Product> findAllFromUser(int id) {
        return repository.findAllByUserId(id);
    }

    @Override
    public int create(Product product) {
        ProductStatus status = ProductStatus.ON_SALE;
        if (product.getCost() >= 100) {
            status = ProductStatus.ON_MODERATION;
        }
        product = new Product(product.getId(), product.getName(), product.getDescription(), product.getCost(), status, product.getSellerId(), product.getBuyerId());
        return repository.save(product).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot save product to database!"));
    }

    @Override
    public void edit(Product product) {
        if (getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || (product.getSellerId().equals(getContext().getAuthentication().getPrincipal()) && !product.getStatus().equals(ProductStatus.BLOCKED))) {
            repository.update(product);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public int deleteById(int id) {
        if (getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || findById(id).getSellerId().equals(getContext().getAuthentication().getPrincipal())) {
            return repository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void makeSold(int productId) {
        Product product = findById(productId);
        product = new Product(product.getId(), product.getName(), product.getDescription(), product.getCost(), ProductStatus.SOLD, product.getSellerId(), (Integer) getContext().getAuthentication().getPrincipal());
        repository.update(product);
    }
}
