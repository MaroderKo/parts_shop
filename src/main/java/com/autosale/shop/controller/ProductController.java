package com.autosale.shop.controller;

import com.autosale.shop.model.Pagination;
import com.autosale.shop.model.Product;
import com.autosale.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping()
    public ResponseEntity<List<Product>> getAll(@RequestBody Pagination pagination) {
        return ResponseEntity.ok(service.findAll(pagination));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Product>> getAllByStatus(@RequestBody Pagination pagination, @PathVariable String status) {
        return ResponseEntity.ok(service.findByStatus(pagination, status));
    }

    @GetMapping("/personal")
    public ResponseEntity<List<Product>> getAllByUser(@RequestBody Pagination pagination) {
        return ResponseEntity.ok(service.findAllFromCurrentUser(pagination));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable int id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Product>> getAllByUserId(@PathVariable int id, @RequestBody Pagination pagination) {
        return ResponseEntity.ok(service.findAllByUserId(id, pagination));
    }

    @PostMapping()
    public ResponseEntity<Integer> create(@RequestBody Product product) {
        return ResponseEntity.ok(service.create(product));
    }

    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody Product product) {
        service.edit(product);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> delete(@PathVariable int id) {
        return ResponseEntity.ok(service.deleteById(id));
    }

    @PostMapping("/buy/{id}")
    public ResponseEntity<Void> buy(@PathVariable int id) {
        service.buy(id);
        return ResponseEntity.ok().build();
    }
}
