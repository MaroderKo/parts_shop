package com.autosale.shop.service.impl;

import com.autosale.shop.model.PaginationRequest;
import com.autosale.shop.model.Product;
import com.autosale.shop.model.ProductStatus;
import com.autosale.shop.repository.AmazonS3ProductRepository;
import com.autosale.shop.repository.ProductRepository;
import com.autosale.shop.service.ProductAwsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAwsServiceImpl implements ProductAwsService {

    private final AmazonS3ProductRepository awsRepository;
    private final ProductRepository repository;

    @Override
    public void save() {
        List<Product> products = repository.findAllByStatus(new PaginationRequest(Integer.MAX_VALUE, 1), "SOLD");
        awsRepository.save(products);
    }

    @Override
    @Transactional
    public void load(String name) {
        List<Product> products = awsRepository.load(name);
        for (Product product : products) {
            if (repository.findById(product.getId()).isPresent()) {
                repository.update(product);
            }
            else {
                repository.save(product);
            }
        }
    }

    @Scheduled(cron = "0 0 23 ? * *")
    @Transactional
    public void autoSave() {
        List<Product> products = repository.findAllByStatus(new PaginationRequest(Integer.MAX_VALUE, 1), ProductStatus.SOLD.toString());
        awsRepository.save(products);
        for(Product product : products) {
            repository.deleteById(product.getId());
        }
    }
}
