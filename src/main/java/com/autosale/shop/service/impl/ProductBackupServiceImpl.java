package com.autosale.shop.service.impl;

import com.autosale.shop.model.PaginationRequest;
import com.autosale.shop.model.Product;
import com.autosale.shop.model.ProductStatus;
import com.autosale.shop.repository.AmazonS3ProductClientRepository;
import com.autosale.shop.repository.ProductRepository;
import com.autosale.shop.service.CsvObjectMapperService;
import com.autosale.shop.service.ProductBackupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductBackupServiceImpl implements ProductBackupService {

    //Polymorphism
    //Якби існував клас що б по іншому імплементував-би один з наступних інтерфейсів то його можна було б замінити на той що є зараз без втрати функціоналу додатку


    private final CsvObjectMapperService objectMapperService;
    private final AmazonS3ProductClientRepository awsRepository;
    private final ProductRepository repository;



    @Override
    public void save() {
        List<Product> products = getSoldProducts();
        awsRepository.save(encode(products));
    }

    @Override
    public void restore(LocalDate date) {
        String name = date+".csv";
        List<Product> products;
        String productsData = awsRepository.load(name);
        products = objectMapperService.decode(productsData, Product.class);
        repository.saveAllIgnoreExistence(products);
    }

    @Scheduled(cron = "0 0 23 ? * *")
    @Transactional
    public void saveAndClear() {
        List<Product> products = getSoldProducts();
        awsRepository.save(encode(products));
        repository.deleteByIdInArray(products.stream().map(Product::getId).toList());
    }

    private String encode(List<Product> products) {
        return objectMapperService.encode(products, Product.class);
    }

    private List<Product> getSoldProducts() {
        return repository.findAllByStatus(new PaginationRequest(Integer.MAX_VALUE, 1), ProductStatus.SOLD.toString());
    }
}
