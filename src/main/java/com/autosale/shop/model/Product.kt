package com.autosale.shop.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Product(
    @JsonProperty("id") val id: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("cost") val cost: Float,
    @JsonProperty("status") val status: ProductStatus?,
    @JsonProperty("seller_id") val sellerId: Int?,
    @JsonProperty("buyer_id") val buyerId: Int?
)
