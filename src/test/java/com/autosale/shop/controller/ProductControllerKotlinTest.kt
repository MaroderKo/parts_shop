package com.autosale.shop.controller

import com.autosale.shop.generator.ProductGenerator
import com.autosale.shop.model.PaginationRequest
import com.autosale.shop.model.PaginationResponse
import com.autosale.shop.model.ProductStatus
import com.autosale.shop.service.ProductService
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.nio.charset.StandardCharsets

class ProductControllerKotlinTest {

    private val service: ProductService = Mockito.mock(ProductService::class.java)
    private val controller: ProductController = ProductController(service)
    private val mock: MockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    private val mapper: ObjectMapper = ObjectMapper()

    @Test
    @Throws(Exception::class)
    fun findAll() {
        val products = listOf(
            ProductGenerator.generate(null, 1, null),
            ProductGenerator.generate(null, 1, null),
            ProductGenerator.generate(null, 1, null)
        )
        val paginationRequest = PaginationRequest()
        Mockito.`when`(service.findByStatus(paginationRequest, null))
            .thenReturn(PaginationResponse(products, paginationRequest, 3))
        val result = mock.perform(
            MockMvcRequestBuilders.get("/products")
                .content(mapper.writeValueAsString(paginationRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(
                MockMvcResultMatchers.content()
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andReturn()
        MatcherAssert.assertThat(
            result.response.contentAsString,
            CoreMatchers.`is`(mapper.writeValueAsString(PaginationResponse(products, paginationRequest, 3)))
        )
    }

    @Test
    @Throws(Exception::class)
    fun findById() {
        val product = ProductGenerator.generate(null, 1, null)
        Mockito.`when`(service.findById(product.id)).thenReturn(product)
        val result = mock.perform(
            MockMvcRequestBuilders.get("/products/" + product.id)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andReturn()
        MatcherAssert.assertThat(HttpStatus.OK.value(), CoreMatchers.`is`(result.response.status))
        MatcherAssert.assertThat(
            String(result.response.contentAsByteArray, StandardCharsets.UTF_8),
            CoreMatchers.`is`(mapper.writeValueAsString(product))
        )
    }

    @Test
    @Throws(Exception::class)
    fun getAllActive() {
        val products = listOf(
            ProductGenerator.generate(null, 1, null),
            ProductGenerator.generate(null, 1, null),
            ProductGenerator.generate(null, 1, null)
        )
        val paginationRequest = PaginationRequest()
        Mockito.`when`(service.findByStatus(paginationRequest, ProductStatus.ON_SALE))
            .thenReturn(PaginationResponse(products, paginationRequest, 3))
        val result = mock.perform(
            MockMvcRequestBuilders.get("/products?status=ON_SALE").content(mapper.writeValueAsString(paginationRequest))
                .contentType(
                    MediaType.APPLICATION_JSON
                )
        )
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).andReturn()
        MatcherAssert.assertThat(
            result.response.contentAsString,
            CoreMatchers.`is`(mapper.writeValueAsString(PaginationResponse(products, paginationRequest, 3)))
        )
    }

    @Test
    @Throws(Exception::class)
    fun getAllByUser() {
        val products = listOf(
            ProductGenerator.generate(null, 1, null),
            ProductGenerator.generate(null, 1, null),
            ProductGenerator.generate(null, 1, null)
        )
        Mockito.`when`(service.findAllFromCurrentUser()).thenReturn(products)
        val result = mock.perform(MockMvcRequestBuilders.get("/products/personal"))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).andReturn()
        MatcherAssert.assertThat(
            result.response.contentAsString,
            CoreMatchers.`is`(mapper.writeValueAsString(products))
        )
    }

    @Test
    @Throws(Exception::class)
    fun getAllBySeller() {
        val products = listOf(
            ProductGenerator.generate(null, 1, null),
            ProductGenerator.generate(null, 1, null),
            ProductGenerator.generate(null, 1, null)
        )
        Mockito.`when`(service.findAllByUserId(1)).thenReturn(products)
        val result = mock.perform(MockMvcRequestBuilders.get("/products/user/1"))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
        MatcherAssert.assertThat(HttpStatus.OK.value(), CoreMatchers.`is`(result.response.status))
        MatcherAssert.assertThat(
            String(result.response.contentAsByteArray, StandardCharsets.UTF_8),
            CoreMatchers.`is`(mapper.writeValueAsString(products))
        )
    }

    @Test
    @Throws(Exception::class)
    fun create() {
        val product = ProductGenerator.generate(null, 1, null)
        Mockito.`when`(service.create(product)).thenReturn(product.id)
        val result = mock.perform(
            MockMvcRequestBuilders.post("/products")
                .content(mapper.writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andReturn()
        MatcherAssert.assertThat(HttpStatus.OK.value(), CoreMatchers.`is`(result.response.status))
    }


    @Test
    @Throws(Exception::class)
    fun delete() {
        val (id) = ProductGenerator.generate(null, 1, null)
        Mockito.`when`(service.deleteById(id)).thenReturn(1)
        val result = mock.perform(
            MockMvcRequestBuilders.delete("/products/$id")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andReturn()
        MatcherAssert.assertThat(HttpStatus.OK.value(), CoreMatchers.`is`(result.response.status))
        MatcherAssert.assertThat(
            String(result.response.contentAsByteArray, StandardCharsets.UTF_8),
            CoreMatchers.`is`("1")
        )
    }

    @Test
    @Throws(Exception::class)
    fun update() {
        val product = ProductGenerator.generate(null, 1, null)
        val result = mock.perform(
            MockMvcRequestBuilders.put("/products")
                .content(mapper.writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andReturn()
        MatcherAssert.assertThat(HttpStatus.OK.value(), CoreMatchers.`is`(result.response.status))
        Mockito.verify(service).edit(product)
    }

    @Test
    @Throws(Exception::class)
    fun buy() {
        val product = ProductGenerator.generate(null, 1, null)
        Mockito.`when`(service.findById(product.id)).thenReturn(product)
        val result = mock.perform(
            MockMvcRequestBuilders.post("/products/buy/" + product.id)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andReturn()
        MatcherAssert.assertThat(HttpStatus.OK.value(), CoreMatchers.`is`(result.response.status))
        Mockito.verify(service).buy(product.id)
    }
}