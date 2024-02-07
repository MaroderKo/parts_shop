package com.autosale.shop.controller;

import com.autosale.shop.model.PaginationRequest;
import com.autosale.shop.model.PaginationResponse;
import com.autosale.shop.model.Product;
import com.autosale.shop.model.ProductStatus;
import com.autosale.shop.service.ProductService;
import com.autosale.shop.util.MetricUtil;
import io.micrometer.core.annotation.Timed;
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
    public ResponseEntity<PaginationResponse<Product>> getAllByStatus(@RequestBody PaginationRequest paginationRequest, @RequestParam(required = false) ProductStatus status) {
        return ResponseEntity.ok(service.findByStatus(paginationRequest, status));
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
    public ResponseEntity<List<Product>> getAllByUserId(@PathVariable int id) {
        return ResponseEntity.ok(service.findAllByUserId(id));
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
    @Timed(value = MetricUtil.Timer.PURCHASE_TIME)
    public ResponseEntity<Void> buy(@PathVariable int id) {
        service.buy(id);
        return ResponseEntity.ok().build();
    }
}
