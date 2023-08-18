package com.autosale.shop.service;


import com.autosale.shop.exception.PermissionDeniedException;
import com.autosale.shop.model.PaginationRequest;
import com.autosale.shop.model.PaginationResponse;
import com.autosale.shop.model.Product;
import com.autosale.shop.model.ProductStatus;
import com.autosale.shop.repository.ProductRepository;
import com.autosale.shop.repository.impl.ProductRepositoryImpl;
import com.autosale.shop.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.autosale.shop.generator.ProductGenerator.generate;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    private final ProductRepository repository = Mockito.mock(ProductRepositoryImpl.class);

    //TODO: fix tests
    private final ProductService productService = new ProductServiceImpl(repository, null);

    @AfterEach
    void contextClear() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void findAll() {
        Product user1 = generate(null, 0, null);
        Product user2 = generate(null, 0, null);
        Product user3 = generate(null, 0, null);

        List<Product> users = List.of(user1, user2, user3);
        when(repository.findAllByStatus(new PaginationRequest(), null)).thenReturn(users);
        PaginationResponse<Product> response = productService.findByStatus(new PaginationRequest(), null);

        assertThat(response.getContent(), containsInAnyOrder(user1, user2, user3));
    }

    @Test
    void findById() {
        Product product = generate(null, 0, null);
        when(repository.findById(product.getId())).thenReturn(Optional.of(product));

        Product returned = productService.findById(product.getId());
        assertThat(returned, is(product));
    }

    @Test
    void findById_with_illegal_id() {
        when(repository.findById(-1)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> productService.findById(-1));
    }

    @Test
    void findByStatus() {
        PaginationRequest paginationRequest = new PaginationRequest();
        when(repository.findAllByStatus(paginationRequest, ProductStatus.ON_SALE.toString())).thenReturn(Collections.emptyList());
        productService.findByStatus(paginationRequest, ProductStatus.ON_SALE);
        verify(repository).findAllByStatus(paginationRequest, ProductStatus.ON_SALE.toString());
    }

    @Test
    void findAllFromCurrentUser() {
        setUserAuthenticationContext();
        when(repository.findAllByUserId(3)).thenReturn(Collections.emptyList());
        productService.findAllFromCurrentUser();
        verify(repository).findAllByUserId(3);

    }

    @Test
    void findAllFromUser() {
        int userId = new Random().nextInt();
        when(repository.findAllByUserId(userId)).thenReturn(Collections.emptyList());
        productService.findAllByUserId(userId);
        verify(repository).findAllByUserId(userId);
    }

    @Test
    void create_with_low_price() {
        Product product = generate(null, 3, 50f);
        when(repository.save(new Product(product.getId(), product.getName(), product.getDescription(), product.getCost(), ProductStatus.ON_SALE, product.getSellerId(), product.getBuyerId()))).thenReturn(Optional.of(product.getId()));
        assertThat(productService.create(product), is(product.getId()));
    }

    @Test
    void create_with_high_price() {
        Product product = generate(null, 3, 150f);
        when(repository.save(new Product(product.getId(), product.getName(), product.getDescription(), product.getCost(), ProductStatus.ON_MODERATION, product.getSellerId(), product.getBuyerId()))).thenReturn(Optional.of(product.getId()));
        assertThat(productService.create(product), is(product.getId()));
    }

    @Test
    void edit() {
        setUserAuthenticationContext();
        Product product = generate(ProductStatus.ON_SALE, 3, null);
        productService.edit(product);
        verify(repository).update(product);
    }

    @Test
    void edit_blocked() {
        setUserAuthenticationContext();
        Product product = generate(ProductStatus.BLOCKED, 3, null);
        assertThrows(PermissionDeniedException.class, () -> productService.edit(product));

    }

    @Test
    void edit_blocked_as_admin() {
        setAdminAuthenticationContext();
        Product product = generate(ProductStatus.BLOCKED, 3, null);
        productService.edit(product);
        verify(repository).update(product);
    }

    @Test
    void edit_not_owned() {
        setUserAuthenticationContext();
        Product product = generate(null, 4, null);
        assertThrows(PermissionDeniedException.class, () -> productService.edit(product));
    }

    @Test
    void edit_not_owned_as_admin() {
        setAdminAuthenticationContext();
        Product product = generate(null, 4, null);
        productService.edit(product);
        verify(repository).update(product);
    }


    @Test
    void delete() {
        setUserAuthenticationContext();
        Product product = generate(null, 3, null);
        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        when(repository.deleteById(product.getId())).thenReturn(1);
        assertThat(productService.deleteById(product.getId()), is(1));
    }

    @Test
    void delete_not_owned() {
        setUserAuthenticationContext();
        Product product = generate(null, 4, null);
        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        when(repository.deleteById(product.getId())).thenReturn(1);
        assertThrows(PermissionDeniedException.class, () -> productService.deleteById(product.getId()));
    }

    @Test
    void delete_not_owned_as_admin() {
        setAdminAuthenticationContext();
        Product product = generate(null, 4, null);
        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        when(repository.deleteById(product.getId())).thenReturn(1);
        assertThat(productService.deleteById(product.getId()), is(1));
    }

    @Test
    @Disabled(value = "Not implemented yet")
    void makeSold() {
        SecurityContextHolder.setContext(new SecurityContextImpl(new UsernamePasswordAuthenticationToken(4, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))));
        Product product = generate(ProductStatus.ON_SALE, 3, 80f);
        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        productService.buy(product.getId());
        verify(repository).update(new Product(product.getId(), product.getName(), product.getDescription(), product.getCost(), ProductStatus.SOLD, product.getSellerId(), 4));
    }

    private void setUserAuthenticationContext() {
        SecurityContextHolder.setContext(new SecurityContextImpl(new UsernamePasswordAuthenticationToken(3, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))));
    }

    private void setAdminAuthenticationContext() {
        SecurityContextHolder.setContext(new SecurityContextImpl(new UsernamePasswordAuthenticationToken(3, null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))));
    }
}
