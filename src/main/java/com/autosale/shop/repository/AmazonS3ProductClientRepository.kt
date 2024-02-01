package com.autosale.shop.repository

interface AmazonS3ProductClientRepository {
    fun save(data: String)
    fun load(name: String): String
}
