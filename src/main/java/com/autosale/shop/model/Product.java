package com.autosale.shop.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Product {

    Integer id;
    String name;
    String description;
    Float cost;
    ProductStatus status;
    Integer sellerId;
    Integer buyerId;

    public Product(@JsonProperty("id") Integer id, @JsonProperty("name") String name, @JsonProperty("description") String description, @JsonProperty("cost") Float cost, @JsonProperty("status") ProductStatus status, @JsonProperty("seller_id") Integer sellerId, @JsonProperty("buyer_id") Integer buyerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.status = status;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
    }
}
