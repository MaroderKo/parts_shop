package com.autosale.shop.service.impl;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.autosale.shop.model.PaginationRequest;
import com.autosale.shop.model.Product;
import com.autosale.shop.model.ProductStatus;
import com.autosale.shop.repository.AmazonS3ProductClientRepository;
import com.autosale.shop.repository.ProductRepository;
import com.autosale.shop.service.CsvObjectMapperService;
import com.autosale.shop.service.ProductBackupService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductBackupServiceImpl implements ProductBackupService {

    private final CsvObjectMapperService objectMapperService;
    private final AmazonS3ProductClientRepository awsRepository;
    private final ProductRepository repository;

    @Override
    public void save() {
        List<Product> products = getListOfSoldProducts();
        try {
            awsRepository.save(encode(products));
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void restore(LocalDate date) {
        String name = date+".csv";
        List<Product> products;
        try {
            String productsData = awsRepository.load(name);
            products = objectMapperService.decode(productsData, Product.class);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (AmazonS3Exception e)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Backup file with name "+name+" not found!");
        }
        for (Product product : products) {
            repository.saveIgnoreExistence(product);
        }
    }

    @Scheduled(cron = "0 0 23 ? * *")
    @Transactional
    public void saveAndClear() {
        List<Product> products = getListOfSoldProducts();
        try {
            awsRepository.save(encode(products));
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        repository.deleteByIdInArray(products.stream().map(Product::getId).toList());
    }

    private String encode(List<Product> products) {
        return objectMapperService.encode(products, Product.class);
    }

    private List<Product> getListOfSoldProducts()
    {
        return repository.findAllByStatus(new PaginationRequest(Integer.MAX_VALUE, 1), ProductStatus.SOLD.toString());
    }
}
