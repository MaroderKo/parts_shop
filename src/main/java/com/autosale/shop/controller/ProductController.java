package com.autosale.shop.controller;

import com.autosale.shop.model.Product;
import com.autosale.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping("/")
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Product>> getAllActive() {
        return ResponseEntity.ok(service.findAllActive());
    }

    @GetMapping("/personal")
    public ResponseEntity<List<Product>> getAllByUser() {
        return ResponseEntity.ok(service.findAllFromCurrentUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable int id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Product>> getAllBySeller(@PathVariable int id) {
        return ResponseEntity.ok(service.findAllFromUser(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Integer> create(@RequestBody Product product) {
        return ResponseEntity.ok(service.create(product));
    }

    @PutMapping("/update")
    public ResponseEntity<Void> update(@RequestBody Product product) {
        service.edit(product);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Integer> delete(@PathVariable int id) {
        return ResponseEntity.ok(service.deleteById(id));
    }

    @PostMapping("/buy/{id}")
    public ResponseEntity<Void> buy(@PathVariable int id) {
        service.makeSold(id);
        return ResponseEntity.ok().build();
    }
}
