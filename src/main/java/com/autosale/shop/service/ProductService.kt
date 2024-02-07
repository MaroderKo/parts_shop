package com.autosale.shop.service

import com.autosale.shop.model.PaginationRequest
import com.autosale.shop.model.PaginationResponse
import com.autosale.shop.model.Product
import com.autosale.shop.model.ProductStatus
import org.springframework.stereotype.Service

@Service
interface ProductService {
    fun findByStatus(paginationRequest: PaginationRequest, status: ProductStatus?): PaginationResponse<Product>
    fun findById(id: Int): Product
    fun findAllFromCurrentUser(): List<Product>
    fun findAllByUserId(id: Int): List<Product>
    fun create(product: Product): Int
    fun edit(product: Product)
    fun deleteById(id: Int): Int
    fun buy(productId: Int)
    fun countAllActive(): Int
}
