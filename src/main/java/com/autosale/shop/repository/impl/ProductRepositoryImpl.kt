package com.autosale.shop.repository.impl

import com.autosale.shop.model.PaginationRequest
import com.autosale.shop.model.Product
import com.autosale.shop.repository.ProductRepository
import org.jooq.Condition
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(private val dsl: DSLContext) : ProductRepository {
    
    override fun save(product: Product): Int {
        return dsl.insertInto(structure.tables.Product.PRODUCT)
            .set(dsl.newRecord(structure.tables.Product.PRODUCT, product))
            .returningResult(structure.tables.Product.PRODUCT.ID)
            .fetchOneInto(Int::class.java)!!
    }

    override fun findById(id: Int): Product? {
        return dsl.selectFrom(structure.tables.Product.PRODUCT)
            .where(structure.tables.Product.PRODUCT.ID.eq(id))
            .fetchOneInto(Product::class.java)
    }

    override fun update(product: Product) {
        dsl.newRecord(structure.tables.Product.PRODUCT, product)
            .update()
    }

    override fun deleteById(id: Int): Int {
        return dsl.deleteFrom(structure.tables.Product.PRODUCT)
            .where(structure.tables.Product.PRODUCT.ID.eq(id))
            .execute()
    }

    override fun deleteByIdInArray(ids: List<Int>): Int {
        return dsl.deleteFrom(structure.tables.Product.PRODUCT)
            .where(structure.tables.Product.PRODUCT.ID.`in`(ids))
            .execute()
    }

    override fun findAllByUserId(userId: Int): List<Product> {
        return findAllWithConditions(listOf(structure.tables.Product.PRODUCT.SELLER_ID.eq(userId)))
    }

    override fun findAllByStatus(pageRequest: PaginationRequest, status: String?): List<Product> {
        return if (status != null) {
            findAllWithConditionsPageable(
                pageRequest,
                listOf(structure.tables.Product.PRODUCT.STATUS.eq(status))
            )
        } else {
            findAllWithConditionsPageable(pageRequest, emptyList())
        }
    }

    override fun countAllByStatus(pageRequest: PaginationRequest, status: String?): Int {
        return if (status != null) {
            countAllWithConditions(listOf(structure.tables.Product.PRODUCT.STATUS.eq(status)))
        } else {
            countAllWithConditions(emptyList())
        }
    }

    override fun saveAllIgnoreExistence(products: List<Product>) {
        dsl.batchStore(products.map { p: Product -> dsl.newRecord(structure.tables.Product.PRODUCT, p) })
            .execute()
    }

    private fun findAllWithConditions(conditions: List<Condition>): List<Product> {
        return dsl.selectFrom(structure.tables.Product.PRODUCT)
            .where(conditions)
            .fetchInto(Product::class.java)
    }

    private fun findAllWithConditionsPageable(
        paginationRequest: PaginationRequest,
        conditions: List<Condition>
    ): List<Product> {
        return dsl.selectFrom(structure.tables.Product.PRODUCT)
            .where(conditions)
            .offset((paginationRequest.currentPage - 1) * paginationRequest.pageSize) // -1 because we start pages from 1
            .limit(paginationRequest.pageSize)
            .fetchInto(Product::class.java)
    }

    private fun countAllWithConditions(conditions: List<Condition>): Int {
        return dsl.fetchCount(structure.tables.Product.PRODUCT, conditions)
    }
}
