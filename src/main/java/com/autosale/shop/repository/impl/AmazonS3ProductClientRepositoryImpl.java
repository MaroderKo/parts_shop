package com.autosale.shop.repository.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.autosale.shop.model.Product;
import com.autosale.shop.repository.AmazonS3ProductClientRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class AmazonS3ProductClientRepositoryImpl implements AmazonS3ProductClientRepository {

    private final AmazonS3 s3;
    @Override
    public void save(String data) throws JsonProcessingException {
            s3.putObject("partsshop", LocalDate.now() + ".csv", data);
    }

    @Override
    public String load(String name) throws IOException {
        try(S3Object s3Object = s3.getObject("partsshop", name)) {
            return new String(s3Object.getObjectContent().readAllBytes());
        }
    }
}
