package com.autosale.shop.repository;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface AmazonS3ProductClientRepository {
    void save(String data) throws JsonProcessingException;

    String load(String name) throws IOException;
}
