package com.autosale.shop.controller;

import com.autosale.shop.model.PaginationRequest;
import com.autosale.shop.model.PaginationResponse;
import com.autosale.shop.model.Product;
import com.autosale.shop.model.ProductStatus;
import com.autosale.shop.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.autosale.shop.generator.ProductGenerator.generate;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class ProductControllerTest {
    private final ProductService service = Mockito.mock(ProductService.class);
    private final ProductController controller = new ProductController(service);
    private final MockMvc mock = MockMvcBuilders.standaloneSetup(controller).build();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void findAll() throws Exception {
        List<Product> products = List.of(generate(null, 1, null), generate(null, 1, null), generate(null, 1, null));
        PaginationRequest paginationRequest = new PaginationRequest();
        when(service.findByStatus(paginationRequest, null)).thenReturn(new PaginationResponse<>(products, paginationRequest, 3));
        MvcResult result = mock.perform(MockMvcRequestBuilders.get("/products").content(mapper.writeValueAsString(paginationRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        assertThat(result.getResponse().getContentAsString(), is(mapper.writeValueAsString(new PaginationResponse<>(products, paginationRequest, 3))));
    }

    @Test
    void findById() throws Exception {
        Product product = generate(null, 1, null);
        when(service.findById(product.getId())).thenReturn(product);
        MvcResult result = mock.perform(get("/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(HttpStatus.OK.value(), is(result.getResponse().getStatus()));
        assertThat(new String(result.getResponse().getContentAsByteArray(), UTF_8), is(mapper.writeValueAsString(product)));
    }

    @Test
    void getAllActive() throws Exception {
        List<Product> products = List.of(generate(null, 1, null), generate(null, 1, null), generate(null, 1, null));
        PaginationRequest paginationRequest = new PaginationRequest();
        when(service.findByStatus(paginationRequest, ProductStatus.ON_SALE)).thenReturn(new PaginationResponse<>(products, paginationRequest, 3));
        MvcResult result = mock.perform(MockMvcRequestBuilders.get("/products?status=ON_SALE").content(mapper.writeValueAsString(paginationRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        assertThat(result.getResponse().getContentAsString(), is(mapper.writeValueAsString(new PaginationResponse<>(products, paginationRequest, 3))));
    }

    @Test
    void getAllByUser() throws Exception {
        List<Product> products = List.of(generate(null, 1, null), generate(null, 1, null), generate(null, 1, null));
        when(service.findAllFromCurrentUser()).thenReturn(products);
        MvcResult result = mock.perform(MockMvcRequestBuilders.get("/products/personal"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        assertThat(result.getResponse().getContentAsString(), is(mapper.writeValueAsString(products)));
    }

    @Test
    void getAllBySeller() throws Exception {
        List<Product> products = List.of(generate(null, 1, null), generate(null, 1, null), generate(null, 1, null));
        when(service.findAllByUserId(1)).thenReturn(products);
        MvcResult result = mock.perform(get("/products/user/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(HttpStatus.OK.value(), is(result.getResponse().getStatus()));
        assertThat(new String(result.getResponse().getContentAsByteArray(), UTF_8), is(mapper.writeValueAsString(products)));

    }

    @Test
    void create() throws Exception {
        Product product = generate(null, 1, null);
        when(service.create(product)).thenReturn(product.getId());
        MvcResult result = mock.perform(post("/products")
                        .content(mapper.writeValueAsString(product))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(HttpStatus.OK.value(), is(result.getResponse().getStatus()));
    }


    @Test
    void delete() throws Exception {
        Product product = generate(null, 1, null);
        when(service.deleteById(product.getId())).thenReturn(1);
        MvcResult result = mock.perform(MockMvcRequestBuilders.delete("/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(HttpStatus.OK.value(), is(result.getResponse().getStatus()));
        assertThat(new String(result.getResponse().getContentAsByteArray(), UTF_8), is("1"));
    }

    @Test
    void update() throws Exception {
        Product product = generate(null, 1, null);
        MvcResult result = mock.perform(put("/products")
                        .content(mapper.writeValueAsString(product))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(HttpStatus.OK.value(), is(result.getResponse().getStatus()));
        verify(service).edit(product);
    }

    @Test
    void buy() throws Exception {
        Product product = generate(null, 1, null);
        when(service.findById(product.getId())).thenReturn(product);
        MvcResult result = mock.perform(post("/products/buy/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(HttpStatus.OK.value(), is(result.getResponse().getStatus()));
        verify(service).buy(product.getId());
    }
}
