package com.autosale.shop.service.impl

import com.autosale.shop.exception.InvalidOperationException
import com.autosale.shop.exception.PermissionDeniedException
import com.autosale.shop.model.*
import com.autosale.shop.repository.ProductRepository
import com.autosale.shop.service.ProductService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
@Profile("RabbitMQ & !Kafka")
class RabbitMQProductServiceImpl(
    private val repository: ProductRepository,
    private val template: RabbitTemplate
) : ProductService {

    override fun findByStatus(
        paginationRequest: PaginationRequest,
        status: ProductStatus?
    ): PaginationResponse<Product> {
        val statusName = status?.run { toString() }
        val products = repository.findAllByStatus(paginationRequest, statusName)
        val totalAmount = repository.countAllByStatus(paginationRequest, statusName)
        return PaginationResponse(products, paginationRequest, totalAmount)
    }

    override fun findById(id: Int): Product {
        return repository.findById(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            String.format("Product with id %d not found", id)
        )

    }

    override fun findAllFromCurrentUser(): List<Product> {
        val userId = SecurityContextHolder.getContext().authentication.principal as Int
        return repository.findAllByUserId(userId)
    }

    override fun findAllByUserId(id: Int): List<Product> {
        return repository.findAllByUserId(id)
    }

    override fun create(product: Product): Int {
        val productConfigured = product.copy(
            status = if (product.cost >= 100) ProductStatus.ON_SALE else ProductStatus.ON_MODERATION,
            sellerId = product.sellerId ?: SecurityContextHolder.getContext().authentication.principal as Int
        )

        return runCatching { repository.save(productConfigured) }
            .fold(
                onSuccess = { Result.success(it) },
                onFailure = {
                    Result.failure(
                        ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Cannot save product to database!"
                        )
                    )
                }
            ).getOrThrow()

    }

    override fun edit(product: Product) {
        if (SecurityContextHolder.getContext().authentication.authorities.contains(SimpleGrantedAuthority("ROLE_ADMIN")) || product.sellerId == SecurityContextHolder.getContext().authentication.principal && product.status != ProductStatus.BLOCKED) {
            repository.update(product)
        } else {
            throw PermissionDeniedException("You don't have permission to do that!")
        }
    }

    override fun deleteById(id: Int): Int {
        return if (SecurityContextHolder.getContext().authentication.authorities.contains(SimpleGrantedAuthority("ROLE_ADMIN")) || findById(
                id
            ).sellerId == SecurityContextHolder.getContext().authentication.principal
        ) {
            repository.deleteById(id)
        } else {
            throw PermissionDeniedException("You don't have permission to do that!")
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    override fun buy(productId: Int) {
        val (_, _, _, _, status, sellerId) = findById(productId)
        if (status == ProductStatus.ON_SALE && sellerId != SecurityContextHolder.getContext().authentication.principal) {
            template.convertAndSend(
                "shopping",
                SaleInfoDTO((SecurityContextHolder.getContext().authentication.principal as Int), productId)
            )
        } else {
            throw InvalidOperationException("You can't buy this product")
        }
    }

    @RabbitListener(id = "buyGroup", queues = ["shopping"])
    fun buyListener(saleInfo: SaleInfoDTO) {
        println("SaleInfo received")
        var product = findById(saleInfo.productId)
        product = product.copy(
            status = ProductStatus.SOLD,
            buyerId = SecurityContextHolder.getContext().authentication.principal as Int
        )
        repository.update(product)
    }
}
