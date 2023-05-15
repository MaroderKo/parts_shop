package com.autosale.shop.repository.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.autosale.shop.model.Product;
import com.autosale.shop.repository.AmazonS3ProductRepository;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AmazonS3ProductRepositoryImpl implements AmazonS3ProductRepository {

    private final AmazonS3 s3;
    private final CsvMapper csvMapper = new CsvMapper();
    CsvSchema schema = csvMapper.schemaFor(Product.class);

    @Override
    @SneakyThrows
    public void save(List<Product> products) {

        s3.putObject("partsshop", LocalDate.now()+".csv",csvMapper.writer(schema).writeValueAsString(products));
    }

    @Override
    @SneakyThrows
    public List<Product> load(String name) {
        S3Object s3Object = s3.getObject("partsshop", name);
        String data = new String(s3Object.getObjectContent().readAllBytes());
        MappingIterator<Product> it = csvMapper
                .readerFor(Product.class)
                .with(schema)
                .with(CsvParser.Feature.WRAP_AS_ARRAY)
                .readValues(data);
        return it.readAll();
    }
}
