package com.autosale.shop.repository

import com.autosale.shop.model.PaginationRequest
import com.autosale.shop.model.Product

interface ProductRepository {
    fun save(product: Product): Int
    fun findById(id: Int): Product?
    fun update(product: Product)
    fun deleteById(id: Int): Int
    fun deleteByIdInArray(ids: List<Int>): Int
    fun findAllByUserId(userId: Int): List<Product>
    fun findAllByStatus(pageRequest: PaginationRequest, status: String?): List<Product>
    fun countAllByStatus(pageRequest: PaginationRequest, status: String?): Int
    fun saveAllIgnoreExistence(products: List<Product>)
}
