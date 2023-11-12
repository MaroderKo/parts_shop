package com.autosale.shop.service.impl

import com.autosale.shop.model.PaginationRequest
import com.autosale.shop.model.Product
import com.autosale.shop.model.ProductStatus
import com.autosale.shop.repository.AmazonS3ProductClientRepository
import com.autosale.shop.repository.ProductRepository
import com.autosale.shop.service.CsvObjectMapperService
import com.autosale.shop.service.ProductBackupService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class ProductBackupServiceImpl(
    private val objectMapperService: CsvObjectMapperService,
    private val awsRepository: AmazonS3ProductClientRepository,
    private val repository: ProductRepository
) : ProductBackupService {

    override fun save() {
        val products = findSoldProducts()
        awsRepository.save(encode(products))
    }

    override fun restore(date: LocalDate) {
        val name = "$date.csv"
        val products: List<Product>
        val productsData = awsRepository.load(name)
        products = objectMapperService.decode(productsData, Product::class.java)
        repository.saveAllIgnoreExistence(products)
    }

    @Scheduled(cron = "0 0 23 ? * *")
    @Transactional
    fun saveAndClear() {
        val products = findSoldProducts()
        awsRepository.save(encode(products))
        repository.deleteByIdInArray(products.map(Product::id))
    }

    private fun encode(products: List<Product>): String {
        return objectMapperService.encode(products, Product::class.java)
    }

    private fun findSoldProducts(): List<Product> = repository.findAllByStatus(PaginationRequest(Int.MAX_VALUE, 1), ProductStatus.SOLD.toString())
}
