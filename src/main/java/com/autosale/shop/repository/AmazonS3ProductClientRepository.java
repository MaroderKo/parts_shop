package com.autosale.shop.repository;

public interface AmazonS3ProductClientRepository {
    void save(String data);

    String load(String name);
}
