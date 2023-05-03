package com.autosale.shop.generator;

import com.autosale.shop.model.Product;
import com.autosale.shop.model.ProductStatus;

import java.util.Random;
import java.util.UUID;

public class ProductGenerator {

    private static Random rand = new Random();
    public static Product generate(ProductStatus status, int sellerId, Float cost)
    {

        return new Product(rand.nextInt(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), cost == null ? rand.nextFloat() : cost, status, sellerId, null);
    }
}
